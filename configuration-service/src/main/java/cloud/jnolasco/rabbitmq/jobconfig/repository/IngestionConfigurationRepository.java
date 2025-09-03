package cloud.jnolasco.rabbitmq.jobconfig.repository;

import cloud.jnolasco.rabbitmq.jobconfig.entity.IngestionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngestionConfigurationRepository extends JpaRepository<IngestionConfiguration, UUID> {
}
