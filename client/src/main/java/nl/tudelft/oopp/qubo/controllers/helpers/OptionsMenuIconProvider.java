package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class OptionsMenuIconProvider {
    private static ImageView editIcon = new ImageView("/icons/edit.png");
    private static ImageView replyIcon = new ImageView("/icons/reply.png");
    private static ImageView markAsAnsIcon = new ImageView("/icons/mark_as_answered.png");
    private static ImageView banIcon = new ImageView("/icons/ban.png");
    private static ImageView deleteIcon = new ImageView("/icons/delete.png");

    private static int iconSize = 20;

    public static MenuItem getEditIcon() {
        editIcon.setPreserveRatio(true);
        editIcon.setFitHeight(iconSize);

        MenuItem edit = new MenuItem("Edit", editIcon);
        return edit;
    }

    public static MenuItem getReplyIcon() {
        replyIcon.setPreserveRatio(true);
        replyIcon.setFitHeight(iconSize);

        MenuItem reply = new MenuItem("Reply", replyIcon);
        return reply;
    }

    public static MenuItem getDeleteIcon() {
        deleteIcon.setPreserveRatio(true);
        deleteIcon.setFitHeight(iconSize);

        MenuItem delete = new MenuItem("Delete", deleteIcon);
        return delete;
    }

    public static MenuItem getMarkAsAnsIcon() {
        markAsAnsIcon.setPreserveRatio(true);
        markAsAnsIcon.setFitHeight(iconSize);

        MenuItem markAsAns = new MenuItem("Mark As Answered", markAsAnsIcon);
        return markAsAns;
    }

    public static MenuItem getBanIcon() {
        banIcon.setPreserveRatio(true);
        banIcon.setFitHeight(iconSize);

        MenuItem ban = new MenuItem("Ban", banIcon);
        return ban;
    }
}
