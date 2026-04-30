package ch.samira.tesan.kitcord.message;

public class CreateMessageRequest {

    private String content;
    private Long chatId;
    private Long senderId;

    public String getContent() {
        return content;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}