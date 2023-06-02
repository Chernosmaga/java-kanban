import managers.Managers;
import memory.InMemoryManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryManagerTest extends ManagerTest<InMemoryManager> {

    @BeforeEach
    void beforeEach() {
        manager =  new InMemoryManager(Managers.getDefaultHistory());
    }

}
