
package utility.graphics;

/**
 * Used to determine if the internal listener should fire.
 * This is used to prevent internal sets from causing events from firing.
 */
public class BlockListener {
   
   private boolean blocked = false;
   
   public void setBlocked(boolean blocked) {
      this.blocked = blocked;
   }
   
   public boolean isBlocked() {
      return blocked;
   }
   
   /**
    * If not currently blocked, becomes blocked and runs the provided {@link Runnable} unblocking afterwards. If blocked then nothing happens.
    * @param runnable The {@link Runnable} to run.
    */
   public void attemptBlockAndDo(Runnable runnable) {
      if (!blocked) {
         blocked = true;
         runnable.run();
         blocked = false;
      }
   }
   
}
