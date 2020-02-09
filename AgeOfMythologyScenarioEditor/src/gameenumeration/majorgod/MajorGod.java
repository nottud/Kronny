
package gameenumeration.majorgod;

public enum MajorGod {
   
   ZEUS("Zeus"),
   POSEIDON("Poseidon"),
   HADES("Hades"),
   ISIS("Isis"),
   RA("Ra"),
   SET("Set"),
   ODIN("Odin"),
   THOR("Thor"),
   LOKI("Loki"),
   KRONOS("Kronos"),
   ORANOS("Oranos"),
   GAIA("Gaia"),
   FU_XI("Fu Xi"),
   NU_WA("Nü Wa"),
   SHENNONG("Shennong"),
   RANDOM_NO_CHINESE("Random no Chinese"),
   RANDOM("Random"),
   GREEK("Greek"),
   NORSE("Norse"),
   EGYPTIAN("Egyptian"),
   ATLANTEAN("Atlantean"),
   CHINESE("Chinese"),
   NATURE("Nature");
   
   private String name;
   
   private MajorGod(String name) {
      this.name = name;
   }
   
   public String getName() {
      return name;
   }
   
   @Override
   public String toString() {
      return name;
   }
   
}
