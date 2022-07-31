package sg.edu.np.mad.Assignment1;

public class Rewards
{
    public String rewardID;
    public String rewardTitle;
    public Integer rewardPoints;
    public Integer voucherQty;
    public String logoUrl;

    //Constructor
    public Rewards()
    {

    }

    public Rewards(String rewardID, String rewardTitle, Integer rewardPoints, Integer voucherQty, String logoUrl)
    {
        this.rewardID = rewardID;
        this.rewardTitle = rewardTitle;
        this.rewardPoints = rewardPoints;
        this.voucherQty = voucherQty;
        this.logoUrl = logoUrl;
    }

    //Getter & Setter - Get and set method for every attribute / variable
    public String getRewardID()
    {
        return rewardID;
    }

    public void setRewardID(String rewardID)
    {
        this.rewardID = rewardID;
    }

    public String getRewardTitle()
    {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle)
    {
        this.rewardTitle = rewardTitle;
    }

    public Integer getRewardPoints()
    {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints)
    {
        this.rewardPoints = rewardPoints;
    }

    public Integer getVoucherQty()
    {
        return voucherQty;
    }

    public void setVoucherQty(Integer voucherQty)
    {
        this.voucherQty = voucherQty;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }
}
