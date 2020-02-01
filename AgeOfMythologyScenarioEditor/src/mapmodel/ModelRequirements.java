
package mapmodel;

import java.util.List;

import datahandler.location.LocationNotFoundException;

public interface ModelRequirements {
   
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException;
   
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException;
   
}
