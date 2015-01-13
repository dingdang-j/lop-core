package org.lop.modules.json.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.lop.modules.enums.BaseEnum;

/**
 * 枚举JsonSerializer.
 * 
 * @author 潘瑞峥
 * @date 2014年8月27日
 */
@SuppressWarnings( "rawtypes" )
public class EnumSerializer extends JsonSerializer<BaseEnum> {

	/**
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
	 *      com.fasterxml.jackson.core.JsonGenerator,
	 *      com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize( BaseEnum value, JsonGenerator jgen, SerializerProvider provider ) throws IOException, JsonProcessingException {
		if ( null != value ) {
			jgen.writeStartObject();
			jgen.writeFieldName( "value" );
			jgen.writeObject( value.getValue() );
			jgen.writeFieldName( "displayName" );
			jgen.writeString( value.getDisplayName() );
			jgen.writeEndObject();
		} else {
			jgen.writeNull();
		}
	}

}