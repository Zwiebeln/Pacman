/**
 * This class represents Fields that can hold an Item. The Item is safed in the property item.
 * @author Mira
 */
public class ItemField extends Field {
	/**
	 * Item hold by the field.
	 */
	Item item;
	
	/**
	 * sets the specified item as the item hold by the field.
	 * @param item represents the item to be set.
	 */
	public ItemField (Item item) {
		this.item = item;
	}
}
