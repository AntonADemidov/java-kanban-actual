package managers;

import org.junit.jupiter.api.BeforeEach;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {
    @BeforeEach
    void setUp() {
        this.historyManager = new InMemoryHistoryManager();
    }
}