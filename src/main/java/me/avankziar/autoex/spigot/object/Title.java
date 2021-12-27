package main.java.me.avankziar.autoex.spigot.object;

public class Title
{
	private String title;
	private String subTitle;
	private int fadeIn;
	private int stay;
	private int fadeOut;
	
	public Title(String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		setTitle(title);
		setSubTitle(subTitle);
		setFadeIn(fadeIn);
		setStay(stay);
		setFadeOut(fadeOut);
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSubTitle()
	{
		return subTitle;
	}

	public void setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
	}

	public int getFadeIn()
	{
		return fadeIn;
	}

	public void setFadeIn(int fadeIn)
	{
		this.fadeIn = fadeIn;
	}

	public int getStay()
	{
		return stay;
	}

	public void setStay(int stay)
	{
		this.stay = stay;
	}

	public int getFadeOut()
	{
		return fadeOut;
	}

	public void setFadeOut(int fadeOut)
	{
		this.fadeOut = fadeOut;
	}

}
