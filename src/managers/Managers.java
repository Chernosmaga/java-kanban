package managers;

import history.HistoryManager;
import history.InHistoryManager;
import memory.InMemoryManager;
import memory.Manager;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InHistoryManager();
    }

}
