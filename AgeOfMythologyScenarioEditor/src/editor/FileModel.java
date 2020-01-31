
package editor;

import java.io.File;
import java.util.List;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class FileModel implements Observable {
   
   public static final ObserverType<File> FILE_CHANGED = new ObserverType<>();
   public static final ObserverType<List<Byte>> DATA_CHANGED = new ObserverType<>();
   
   private ObservableManager observableManager;
   private File file;
   private List<Byte> data;
   
   public FileModel() {
      observableManager = new ObservableManagerImpl();
      data = null;
   }
   
   public void setFile(File file) {
      this.file = file;
      observableManager.notifyObservers(FILE_CHANGED, file);
   }
   
   public void setData(List<Byte> data) {
      this.data = data;
      observableManager.notifyObservers(DATA_CHANGED, data);
   }
   
   public File getFile() {
      return file;
   }
   
   public List<Byte> getData() {
      return data;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
