package io.github.sashirestela.openai.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OpenAIEventTest {

  @Test
  void shouldReturnTrueWhenRawDataHasTheRightHeader() {
    OpenAIEvent event = new OpenAIEvent("data: This is the actual data.");
    boolean actualCondition = event.isActualData();
    boolean expectedCondition = true;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnFalseWhenRawDataHasTheIncorrectHeader() {
    OpenAIEvent event = new OpenAIEvent("data : This is the actual data.");
    boolean actualCondition = event.isActualData();
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnFalseWhenRawDataIsEmpty() {
    OpenAIEvent event = new OpenAIEvent("");
    boolean actualCondition = event.isActualData();
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnFalseWhenRawDataIsReturn() {
    OpenAIEvent event = new OpenAIEvent("\n");
    boolean actualCondition = event.isActualData();
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  void shouldReturnFalseWhenRawDataHasTheRightHeaderAndTheDoneMark() {
    OpenAIEvent event = new OpenAIEvent("data: [DONE]");
    boolean actualCondition = event.isActualData();
    boolean expectedCondition = false;
    assertEquals(expectedCondition, actualCondition);
  }

  @Test
  @SuppressWarnings("unused")
  void shouldReturnTheActualDataWhenRawDataMeetsConditions() {
    OpenAIEvent event = new OpenAIEvent("data:   This is the actual data.  ");
    String rawData = event.getRawData();
    String actualData = event.getActualData();
    String expectedData = "This is the actual data.";
    assertEquals(expectedData, actualData);
  }

}
