package cloud.jnolasco.rabbitmq.ingestor.service;

import cloud.jnolasco.rabbitmq.common.event.FileUploadEvent;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A reusable service responsible for dynamically ingesting CSV data into the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicCsvIngestor {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Reads a CSV file and dynamically generates and executes SQL INSERT statements.
     * This method is transactional.
     *
     * @param event The file upload event containing the path to the CSV.
     * @param config The job configuration containing the target table and column mappings.
     * @param truncateBeforeLoad If true, the target table will be truncated before new data is inserted.
     */
    @Transactional
    public void ingest(FileUploadEvent event, Map<String, Object> config, boolean truncateBeforeLoad) {
        String targetTable = (String) config.get("targetTable");
        Map<String, String> mappingJson = (Map<String, String>) config.get("mappingJson");

        try {
            if (truncateBeforeLoad) {
                log.warn("Truncating table as per configuration: {}", targetTable);
                jdbcTemplate.execute("TRUNCATE TABLE " + targetTable);
            }

            Map<String, String> columnTypes = getColumnTypes(targetTable);
            String columns = String.join(", ", mappingJson.values());
            String placeholders = mappingJson.values().stream().map(c -> "?").collect(Collectors.joining(", "));
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", targetTable, columns, placeholders);

            log.info("Generated SQL template: {}", sql);

            int rowsProcessed = 0;
            try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new FileReader(event.filePath()))) {
                Map<String, String> csvRecord;
                while ((csvRecord = csvReader.readMap()) != null) {
                    List<Object> queryParams = new ArrayList<>();
                    for (String dbColumn : mappingJson.values()) {
                        String csvHeader = mappingJson.entrySet().stream()
                                .filter(entry -> Objects.equals(entry.getValue(), dbColumn))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse(null);

                        String rawValue = csvRecord.get(csvHeader);
                        String dbType = columnTypes.get(dbColumn);
                        Object convertedValue = convertToType(rawValue, dbType);
                        queryParams.add(convertedValue);
                    }

                    jdbcTemplate.update(sql, queryParams.toArray());
                    rowsProcessed++;
                }
                log.info("Successfully processed and inserted {} rows into table '{}'.", rowsProcessed, targetTable);

            } catch (IOException | CsvValidationException e) {
                log.error("Error reading or parsing CSV file: {}", event.filePath(), e);
                throw new RuntimeException("CSV processing failed", e);
            }
        } catch (DataAccessException e) {
            log.error("Database error during ingestion for file: {}", event.filePath(), e);
            throw e;
        }
    }

    private Map<String, String> getColumnTypes(String tableName) {
        String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = ?";
        return jdbcTemplate.query(sql, rs -> {
            Map<String, String> map = new java.util.HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("column_name"), rs.getString("data_type"));
            }
            return map;
        }, tableName);
    }

    private Object convertToType(String value, String dbType) {
        if (value == null || value.isEmpty() || dbType == null) {
            return null;
        }

        return switch (dbType.toLowerCase()) {
            case "bigint" -> Long.parseLong(value);
            case "integer", "int" -> Integer.parseInt(value);
            case "decimal", "numeric" -> new BigDecimal(value);
            case "boolean" -> Boolean.parseBoolean(value);
            default -> value;
        };
    }
}
