/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.core;

/**
 *
 * @author Diana
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller
{
  
  @FXML
  private Button saveButton;
  @FXML
  private TextField ClientIDTF;
  @FXML
  private TextField ClientNameTF;
  private ObservableList<String> items = FXCollections.observableArrayList(new String[] { "MusicMall" });
  
  public ObservableList<String> getItems()
  {
    return this.items;
  }
  
  public void setItems(ObservableList<String> items)
  {
    this.items = items;
  }
}
