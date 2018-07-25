package dao.util.card;

import entity.Card;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CardTypeConverter implements AttributeConverter<Card.CardType, String> {

  @Override
  public String convertToDatabaseColumn(Card.CardType type) {
    return type.toString().toLowerCase();
  }

  @Override
  public Card.CardType convertToEntityAttribute(String dbData) {
    return Card.CardType.valueOf(dbData.toUpperCase());
  }
}
