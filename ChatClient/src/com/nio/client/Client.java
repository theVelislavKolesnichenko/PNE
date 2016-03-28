package com.nio.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nio.model.Room;
import com.nio.model.User;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Client extends Application {
	
	static List<Room> rooms = new ArrayList<Room>();
	
    void intiRooms()
    {
    	List<User> user = new ArrayList<User>();
    	user.add(new User(1, "User0", "Pass0"));
    	user.add(new User(2, "User1", "Pass1"));
    	user.add(new User(3, "User2", "Pass2"));
    	user.add(new User(4, "User3", "Pass3"));
    	user.add(new User(5, "User4", "Pass4"));
    	
    	rooms.add(new Room(1, "Room0", user));
    	rooms.add(new Room(2, "Room1", user));
    }
    
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Client ChatMon");
		
		intiRooms();
		ListView<Room> listView = new ListView<>();
		ObservableList<Room> myObservableList = FXCollections.observableList(rooms);
        listView.setItems(myObservableList);
		
        listView.setCellFactory(new Callback<ListView<Room>, ListCell<Room>>(){
        	 
            @Override
            public ListCell<Room> call(ListView<Room> p) {
                 
                ListCell<Room> cell = new ListCell<Room>(){
 
                    @Override
                    protected void updateItem(Room r, boolean bln) {
                        super.updateItem(r, bln);
                        if (r != null) {
                        	
                        	VBox vBox = new VBox(new Text(r.getName()), new Text(String.format("%d $", r.getUsers().size())));
                        	Label graphic = new Label("G");
                        	graphic.setFont(new Font(24));
                        	HBox hBox = new HBox(graphic, vBox);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                            
                        	//setText(String.format("%s : %d", r.getName(), r.getUsers().size()));
                        }
                    }
 
                };
                 
                return cell;
            }
        });
        
        listView.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Room>() {

					@Override
					public void changed(ObservableValue<? extends Room> observable, Room oldValue, Room newValue) {
//						Alert alert = new Alert(AlertType.INFORMATION);
//						alert.setTitle(newValue.getName());
//						alert.setHeaderText(String.format("%d", newValue.getId()));
//						alert.setContentText(String.format("%d", newValue.getUsers().size()));
//						alert.showAndWait();
						
						 final Stage newStage = new Stage();	
						 newStage.setTitle(newValue.getName());
			                 
			             Label newLabel = new Label();
			             newLabel.setText(newStage.toString());
			                
			             Label newLabel1 = new Label();
			             newLabel1.setText(String.format("User size: %d", newValue.getUsers().size()));
			             
			             Label newLabel2 = new Label();
			             newLabel2.setText(String.format("Room ID: %d", newValue.getId()));
			             
//			                Button btnClose = new Button("Close");
//			                btnClose.setOnAction(new EventHandler<ActionEvent>() {
//			                    @Override
//			                    public void handle(ActionEvent t) {
//			                        newStage.close();
//			                    }
//			                });
			             
			 			BorderPane newRoot;
						try {
							newRoot = (BorderPane)FXMLLoader.load(getClass().getResource("chat.fxml"));
				 			Scene scene = new Scene(newRoot,500,500);
				 			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				 			primaryStage.setScene(scene);
				 			primaryStage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			             VBox vBox = new VBox(newLabel, newLabel1, newLabel2);
			                 
		                //vBox.getChildren().addAll(newLabel, newLabel1, newLabel2/*, btnClose*/);
//		                Group newRoot = new Group();
//		                newRoot.getChildren().add(vBox);
//		                Scene scene = new Scene(newRoot, 300, 200);
//		                newStage.setScene(scene);
//			            newStage.show();
					}
				});
        
		try {
			StackPane root = new StackPane();
			root.getChildren().add(listView);
			Scene scene = new Scene(root, 300, 250);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
