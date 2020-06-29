package main.java.me.avankziar.automaticexecute.spigot.assistance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackParser
{
	
	public static String ToBase64ItemStack(ItemStack item) throws IllegalStateException  //FIN
    {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeObject(item);
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    public static ItemStack FromBase64ItemStack(String data) throws IOException  //FIN
    {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack items = (ItemStack) dataInput.readObject();
            
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) 
    	{
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    @SuppressWarnings("deprecation")
	public String convertItemStackToJson(ItemStack itemStack) //FIN
	{
		/*
		 * so baut man das manuell
		 * ItemStack is = createHoverItem(p, bookpath+".hover.item."+ar[2], bok);
		 * emptyword.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,new BaseComponent[]{new TextComponent(convertItemStackToJson((is)))}));
		 */
	    // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
	    Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
	    Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

	    // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
	    Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
	    Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
	    Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

	    Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
	    Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
	    Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

	    try {
	        nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
	        nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
	        itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
	    } catch (Throwable t) {
	        t.printStackTrace();
	        return null;
	    }

	    // Return a string representation of the serialized object
	    return itemAsJsonObject.toString();
	}
    
	//private Gson gson;

    /*public ItemStackParser() 
    {
        gson = new GsonBuilder().create();
    }

    public ItemStack deserialize(String json)
    		throws JsonParseException 
    {
    	ItemStack is = gson.fromJson(json, ItemStack.class);
        //Map<String, Object> map = gson.fromJson(new JsonPrimitive(json), new TypeToken<Map<String, Object>>(){}.getType());
        //return ItemStack.deserialize(map);
    	
    	return is;
    }

    public String serialize(ItemStack itemStack) 
    {
    	String json = convertItemStackToJson(itemStack);
    	//String json = gson.toJson(itemStack);
    	//String json = gson.toJson(itemStack.serialize());
        return json;
    }
    
    
    
    /**
     * Get the binary representation of ItemStack
     * for fast ItemStack serialization
     *
     * @param itemStack the item to be serialized
     * @return binary NBT representation of the item stack
     
    @SuppressWarnings("deprecation")
	public static byte[] itemToBinary(ItemStack itemStack) throws ReflectiveOperationException, IOException {
        Class<?> classCraftItemStack = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Class<?> classNativeItemStack = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> classNBTTagCompound = ReflectionUtil.getNMSClass("NBTTagCompound");

        Method asNMSCopy_craftItemStack = ReflectionUtil.getMethod(classCraftItemStack, "asNMSCopy", ItemStack.class);
        Method save_nativeItemStack = ReflectionUtil.getMethod(classNativeItemStack, "save", classNBTTagCompound);
        Method write_nbtTagCompound = ReflectionUtil.getMethod(classNBTTagCompound, "write", DataOutput.class);

        Object nativeItemStack = asNMSCopy_craftItemStack.invoke(null, itemStack);
        Object nbtTagCompound = classNBTTagCompound.newInstance();
        save_nativeItemStack.invoke(nativeItemStack, nbtTagCompound);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        write_nbtTagCompound.invoke(nbtTagCompound, dos);
        byte[] outputByteArray = baos.toByteArray();
        dos.close();
        baos.close();
        return outputByteArray;
    }

    private static Object unlimitedNBTReadLimiter = null;

    @SuppressWarnings({ "deprecation", "rawtypes" })
	public static ItemStack itemFromBinary(byte[] nbt, int offset, int len) throws ReflectiveOperationException, IOException {
        Class<?> classNBTReadLimiter = ReflectionUtil.getNMSClass("NBTReadLimiter");
        if (unlimitedNBTReadLimiter == null) {
            for (Field f : classNBTReadLimiter.getDeclaredFields()) {
                if (f.getType().equals(classNBTReadLimiter)) {
                    unlimitedNBTReadLimiter = f.get(null);
                    break;
                }
            }
        }

        Class<?> classCraftItemStack = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Class<?> classNativeItemStack = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> classNBTTagCompound = ReflectionUtil.getNMSClass("NBTTagCompound");

        Method load_nbtTagCompound = ReflectionUtil.getMethod(classNBTTagCompound, "load", DataInput.class, int.class, classNBTReadLimiter);
        Constructor constructNativeItemStackFromNBTTagCompound = classNativeItemStack.getConstructor(classNBTTagCompound);
        Method asBukkitCopy_CraftItemStack = ReflectionUtil.getMethod(classCraftItemStack, "asBukkitCopy", classNativeItemStack);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nbt, offset, len);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        Object reconstructedNBTTagCompound = classNBTTagCompound.newInstance();
        load_nbtTagCompound.invoke(reconstructedNBTTagCompound, dataInputStream, 0, unlimitedNBTReadLimiter);
        dataInputStream.close();
        byteArrayInputStream.close();
        Object reconstructedNativeItemStack = constructNativeItemStackFromNBTTagCompound.newInstance(reconstructedNBTTagCompound);
        return (ItemStack) asBukkitCopy_CraftItemStack.invoke(null, reconstructedNativeItemStack);
    }
    
    @SuppressWarnings({ "deprecation", "rawtypes" })
	public static ItemStack itemFromBinary(String json) throws ReflectiveOperationException, IOException {
        Class<?> classNBTReadLimiter = ReflectionUtil.getNMSClass("NBTReadLimiter");
        if (unlimitedNBTReadLimiter == null) {
            for (Field f : classNBTReadLimiter.getDeclaredFields()) {
                if (f.getType().equals(classNBTReadLimiter)) {
                    unlimitedNBTReadLimiter = f.get(null);
                    break;
                }
            }
        }

        Class<?> classCraftItemStack = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Class<?> classNativeItemStack = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> classNBTTagCompound = ReflectionUtil.getNMSClass("NBTTagCompound");

        Method load_nbtTagCompound = ReflectionUtil.getMethod(classNBTTagCompound, "load", DataInput.class, int.class, classNBTReadLimiter);
        Constructor constructNativeItemStackFromNBTTagCompound = classNativeItemStack.getConstructor(classNBTTagCompound);
        Method asBukkitCopy_CraftItemStack = ReflectionUtil.getMethod(classCraftItemStack, "asBukkitCopy", classNativeItemStack);

        //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nbt, offset, len);
        //DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        Object reconstructedNBTTagCompound = classNBTTagCompound.newInstance();
        load_nbtTagCompound.invoke(reconstructedNBTTagCompound, json, 0, unlimitedNBTReadLimiter);
        //dataInputStream.close();
        //byteArrayInputStream.close();
        Object reconstructedNativeItemStack = constructNativeItemStackFromNBTTagCompound.newInstance(reconstructedNBTTagCompound);
        return (ItemStack) asBukkitCopy_CraftItemStack.invoke(null, reconstructedNativeItemStack);
    }*/
}
