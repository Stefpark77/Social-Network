package socialnetwork.utils.events;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Group;
import socialnetwork.domain.Message;

public class MessageorGroupChangeEvent implements utils.events.Event {
    private ChangeEvent type;
    private Message data, oldData;
    private Group gdata, goldData;

    public MessageorGroupChangeEvent(ChangeEvent type, Message data) {
        this.type = type;
        this.data = data;
    }
    public MessageorGroupChangeEvent(ChangeEvent type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }
    public MessageorGroupChangeEvent(ChangeEvent type, Group data) {
        this.type = type;
        this.gdata = data;
    }
    public MessageorGroupChangeEvent(ChangeEvent type, Group data, Group oldData) {
        this.type = type;
        this.gdata = data;
        this.goldData=oldData;
    }

    public ChangeEvent getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }

    public Group getGdata() {
        return gdata;
    }

    public Group getGoldData() {
        return goldData;
    }

    @Override
    public String getEventType() {
        return "MessageorGroup";
    }
}
