/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelmatrixmultiply;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * @author Scott Purcell
 * Project 6 ParallelMatrixMultiply
 * cs 3259
 * 11/19/2014
 */

public class ParallelMatrixMultiply extends Application {
    
   
    
    String fileArg;
    String fileArg2;
    DataIn in=new DataIn();
    String output;
    private static final int SCENE_WIDTH = 450;
    private static final int SCENE_HEIGHT = 300;
 
  
     
    @Override
    public void start(Stage primaryStage) {
        
       TextArea ta = new TextArea();
       
        
        // create a scene and place it on the primary stage
        Scene scene = new Scene(new ScrollPane(ta), SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setTitle("ParallelMatrixMultiply");
        primaryStage.setScene(scene);
        primaryStage.show(); 
        
        List <String> unnamedParameters=getParameters().getUnnamed();
        if(unnamedParameters.size()>0)
        {
            fileArg=unnamedParameters.get(0);
            fileArg2=unnamedParameters.get(1);
           
        }
        output=in.setMatrix(fileArg, fileArg2);
        ta.appendText(output);
    }
    
  public static void main(String[] args)
    {
        launch(args);
    }
}
