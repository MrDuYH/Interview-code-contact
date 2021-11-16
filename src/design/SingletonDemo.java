package design;

public class SingletonDemo {
    static class Singleton1 {
        private static final Singleton1 instance = new Singleton1();

        private Singleton1() {
        }

        public static Singleton1 getInstance() {
            return instance;
        }
    }

    static class Singleton2 {
        private Singleton2() {
        }

        public static class SingletonHolder {
            static final Singleton2 instance = new Singleton2();

            public static Singleton2 getInstance() {
                return instance;
            }
        }
    }

    static class Singleton3 {
        private static volatile Singleton3 instance;

        private Singleton3() {
        }

        public static Singleton3 getInstance() {
            if (instance == null) {
                synchronized (Singleton3.class) {
                    if (instance == null) {
                        return new Singleton3();
                    }
                }
            }
            return instance;
        }
    }

    enum Singleton4 {
        INSTANCE(1, "name");

        Singleton4(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int id;
        public String name;
    }
}
