/**
 * This class represents Fields that can not hold an Item. A NoItemField can have different FieldTypes.
 * @author Mira
 */
public class NoItemField extends Field {
	/**
	 * FieldType of the Field
	 */
	FieldType fieldType;
	
	/**
	 * sets the specified fieldType as the fieldType of field.
	 * @param fieldType represents the fieldType to be set.
	 */
	public NoItemField(FieldType fieldType) {
		this.fieldType = fieldType;
	}
}
