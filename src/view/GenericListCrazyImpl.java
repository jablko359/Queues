package view;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 06 sty 2017.
 * 22 : 37
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.UUID;

public class GenericListCrazyImpl<ANY> {

    public int nextId() {
        return anyObservableList.size();
    }

    public interface Callback<ANY> {
        Node recycle(ANY item);
    }

    public GenericListCrazyImpl(Callback<ANY> callback) {
        this.callback = callback;
    }

    Callback<ANY> callback;
    List<ANY> collection;
    ObservableList<Node> anyViews = FXCollections.observableArrayList();
    ObservableList<ANY> anyObservableList;

    public void add(ANY any) {
        anyObservableList.add(any);
        anyViews.add(new Label(UUID.randomUUID().toString()));
    }

    public void setup(Pane node, List<ANY> collection) {
        this.collection = collection;

        anyObservableList = FXCollections.observableArrayList(this.collection);

        ListView<ANY> anyList = new ListView<>(anyObservableList);

        anyList.setCellFactory(param -> new AnyCell());

        node.getChildren().add(anyList);
    }

    private class AnyCell extends ListCell<ANY> {
        //ConversationCellController cl;
        // private final VIEW imageView = new ImageView();

        public AnyCell() {

            //setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setAlignment(Pos.CENTER);

            /**
             setOnDragDetected(event -> {
             if (getItem() == null) {
             return;
             }

             ObservableList<ANY> items = getListView().getItems();

             Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

             ClipboardContent content = new ClipboardContent();
             System.out.println("current index: " + getIndex());
             content.put(new DataFormat(String.valueOf(getIndex())), getItem());
             dragboard.setDragView(anyViews.get(items.indexOf(getItem()))
             );
             dragboard.setContent(content);

             event.consume();
             });

             setOnDragOver(event -> {
             if (event.getGestureSource() != thisCell &&
             event.getDragboard().hasString()) {
             event.acceptTransferModes(TransferMode.MOVE);
             }

             event.consume();
             });

             setOnDragEntered(event -> {
             if (event.getGestureSource() != thisCell &&
             event.getDragboard().hasString()) {
             setOpacity(0.3);
             }
             });

             setOnDragExited(event -> {
             if (event.getGestureSource() != thisCell &&
             event.getDragboard().hasString()) {
             setOpacity(1);
             }
             });

             setOnDragDropped(event -> {
             if (getItem() == null) {
             return;
             }

             Dragboard db = event.getDragboard();
             boolean success = false;

             if (db.hasString()) {
             ObservableList<ANY> items = getListView().getItems();
             int draggedIdx = Integer.valueOf(db.getString());
             int thisIndex = items.indexOf(getItem());

             Image temp = anyViews.get(draggedIdx);
             anyViews.set(draggedIdx, anyViews.get(thisIndex));
             anyViews.set(thisIndex, temp);

             items.set(draggedIdx, getItem());
             items.set(thisIndex, items.get(thisIndex));

             List<ANY> itemsCopy = new ArrayList<>(getListView().getItems());
             getListView().getItems().setAll(itemsCopy);

             success = true;

             System.out.println("AFTER DRAGGING");
             for (ANY any : items) {
             System.out.println(String.valueOf(any));
             }

             }

             event.setDropCompleted(success);

             event.consume();
             });

             setOnDragDone(DragEvent::consume);
             */
        }

        @Override
        protected void updateItem(ANY item, boolean empty) {
            super.updateItem(item, empty);
            System.out.println("updateItem");
            if (empty || item == null) {
                setGraphic(null);
            } else {
                if (callback != null) {
                    Node node = callback.recycle(item);
                    setGraphic(node);
                }
            }
        }
    }
}