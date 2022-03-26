package manager;

import java.io.IOException;

public class Managers {

/* Пока что так и не разобрался с внедрением зависимостей - оставил как есть. Когда прокачаю скил обязательно внедрю.
Осталю замечание в коде, чтобы помнить: "Можно менеджер истории передавать в менеджер задач через конструктор при
создании. Получится внедрение зависимостей, минус одна жесткая связь. https://habr.com/ru/post/350068/"
 */

    public static TaskManager getDefault() throws IOException {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultBackup() throws IOException {
        return new FileBackedTasksManager();
    }
}
