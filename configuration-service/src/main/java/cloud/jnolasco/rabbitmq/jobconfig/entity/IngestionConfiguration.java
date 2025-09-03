package cloud.jnolasco.rabbitmq.jobconfig.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "ingestion_configurations")
@Getter
@Setter
public class IngestionConfiguration {

    @Id
    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Column(name = "target_table", nullable = false)
    private String targetTable;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "mapping_json", nullable = false, columnDefinition = "jsonb")
    private Map<String, String> mappingJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

}