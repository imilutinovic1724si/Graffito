package raf.graffito.dsw.core;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffRepository;
import raf.graffito.dsw.observer.MessageGenerator;
import raf.graffito.dsw.observer.logger.Logger;
import raf.graffito.dsw.observer.logger.LoggerFactory;

public class ApplicationFramework {
    // inicijalizuje celu nasu aplikaciju = jezgro nase aplikacije
    //Singleton

    private static ApplicationFramework instance;
    private MessageGenerator messageGenerator;
    private GraffRepository repository;

    private ApplicationFramework(){

    }
    public static ApplicationFramework getInstance(){
        if(instance == null){
            instance = new ApplicationFramework();
            // initialize() metodu pozivamo u konstruktoru da ne bi imali beskonacnu petlju
            instance.initialize();
        }
        return instance;
    }


    public void initialize(){
        // Kreira repozitorijum, sistem za poruke(u okviru koga imamo logere), i glavni view(MainFrame)
        repository = new GraffRepository();
        messageGenerator = new MessageGenerator();
        Logger consoleLogger = LoggerFactory.createLogger("console");
        Logger fileLogger = LoggerFactory.createLogger("file");

        // Dodajemo nase logere kao Subscriber-e
        messageGenerator.addSubscriber(consoleLogger);
        messageGenerator.addSubscriber(fileLogger);

        // Dodajemo nas MainFrame kao Subscriber-a
        MainFrame mainFrame = MainFrame.getInstance();
        messageGenerator.addSubscriber(mainFrame);
        mainFrame.setVisible(true);
    }

    public MessageGenerator getMessageGenerator() {
        return messageGenerator;
    }
    public GraffRepository getRepository() {
        return repository; ///getter
    }
}
