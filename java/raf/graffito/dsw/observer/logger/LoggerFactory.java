package raf.graffito.dsw.observer.logger;

public class LoggerFactory {

    // ProductFactory kreira Products, u nasem slucaju kreira Logger-e (console i file)

    public static Logger createLogger(String type) {
        if (type == null){
            throw new IllegalArgumentException("Tip logera ne moze da bude null.\n");
        }
        switch (type.toLowerCase()) {
            case "console":
                return new ConsoleLogger();
            case "file":
                return new FileLogger();
            default:
                throw new IllegalArgumentException("Tip logera mora biti Console ili File.\n");
        }
    }
}
