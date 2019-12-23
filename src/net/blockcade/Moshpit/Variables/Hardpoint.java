package net.blockcade.Moshpit.Variables;

import net.blockcade.Arcade.Varables.TeamColors;
import net.blockcade.Moshpit.Main;
import net.blockcade.Moshpit.Utils.BoundedRegion;
import org.bukkit.block.Block;

public class Hardpoint {

    private double time;

    private TeamColors holding_team;

    private PointState state = PointState.AVAILABLE;

    private BoundedRegion location;

    private Block fire;

    public Block getFire() {
        return fire;
    }

    public BoundedRegion getLocation() {
        return location;
    }

    public double getTime() {
        return time;
    }

    public PointState getState() {
        return state;
    }

    public void setFire(Block fire) {
        this.fire = fire;
    }

    public TeamColors getHolding_team() {
        return holding_team;
    }

    public void setHolding_team(TeamColors holding_team) {
        this.holding_team = holding_team;
    }

    public void setLocation(BoundedRegion location) {
        this.location = location;
    }

    public void setState(PointState state) {
        this.state = state;
    }

    public void resetTime() {
        this.time = Main.BASE_POINT_TIME*20;
    }
    public void tickTime(double i) {
        this.time = this.time-i;
    }
}
