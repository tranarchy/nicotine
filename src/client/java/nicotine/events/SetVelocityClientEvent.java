package nicotine.events;

public class SetVelocityClientEvent {
   public double x;
   public double y;
   public double z;

   public SetVelocityClientEvent(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }
}
