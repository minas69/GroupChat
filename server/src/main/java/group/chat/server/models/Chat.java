package group.chat.server.models;

public class Chat {

    private int id;

    private String title;

    private String description;

    private Message lastMessage;

    private int membersCount;

    private String color;

    private boolean isEphemeral;

    private Member admin;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Message getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getMembersCount() {
        return membersCount;
    }
    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEphemeral() {
        return isEphemeral;
    }

    public void setEphemeral(boolean ephemeral) {
        isEphemeral = ephemeral;
    }

    public Member getAdmin() {
        return admin;
    }

    public void setAdmin(Member admin) {
        this.admin = admin;
    }
}
