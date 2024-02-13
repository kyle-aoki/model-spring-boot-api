package aoki.kyle.model;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SuppressWarnings("resource")
public class BaseTest {

    private static final int POSTGRES_CONTAINER_PORT = 5432;
    private static final int POSTGRES_LOCAL_PORT = 5433;

    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(POSTGRES_CONTAINER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(new HostConfig().withPortBindings(
                    new PortBinding(Ports.Binding.bindPort(POSTGRES_LOCAL_PORT), new ExposedPort(POSTGRES_CONTAINER_PORT)))));


    private static final int REDIS_CONTAINER_PORT = 6379;
    private static final int REDIS_LOCAL_PORT = 6380;

    private static final GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:5.0.3-alpine"))
            .withExposedPorts(REDIS_CONTAINER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(new HostConfig().withPortBindings(
                    new PortBinding(Ports.Binding.bindPort(REDIS_LOCAL_PORT), new ExposedPort(REDIS_CONTAINER_PORT)))));

    @BeforeAll
    public static void setUp() {
        postgresqlContainer.start();
        redis.start();
    }

    @AfterAll
    public static void tearDown() {
        postgresqlContainer.stop();
        redis.stop();
    }
}
