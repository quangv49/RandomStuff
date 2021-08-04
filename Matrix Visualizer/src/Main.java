import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleDoubleProperty;

public class Main extends Application {
	private static final double AXIS_LENGTH = 500;
	private static final double AXIS_START = 50;
	private static final double ORIGIN = AXIS_START + AXIS_LENGTH / 2;
	private static double maxBounds = 2;
	
	public void start(Stage primaryStage) {
		BorderPane masterPane = new BorderPane();
		GridPane matrixPane = new GridPane();
		
		Button button = new Button("Update");
		Pane planePane = new Pane();
		Line xAxis = new Line();
		Line yAxis = new Line();
		Line line1 = new Line();
		Line line2 = new Line();
		Line line3 = new Line();
		Line line4 = new Line();
		Text matrix = new Text("Matrix");
		Text max = new Text("M = 2.0");
		Text M1 = new Text("M"), M2 = new Text("M");
		Text negM1 = new Text("-M"), negM2 = new Text("-M");
		TextField a11 = new TextField();
		TextField a12 = new TextField();
		TextField a21 = new TextField();
		TextField a22 = new TextField();
		
		SimpleDoubleProperty x1 = new SimpleDoubleProperty(1.0);
		SimpleDoubleProperty y1 = new SimpleDoubleProperty(1.0);
		SimpleDoubleProperty x2 = new SimpleDoubleProperty(-1.0);
		SimpleDoubleProperty y2 = new SimpleDoubleProperty(1.0);
		SimpleDoubleProperty x3 = new SimpleDoubleProperty(-1.0);
		SimpleDoubleProperty y3 = new SimpleDoubleProperty(-1.0);
		SimpleDoubleProperty x4 = new SimpleDoubleProperty(1.0);
		SimpleDoubleProperty y4 = new SimpleDoubleProperty(-1.0);		
		
		a11.setMaxSize(50,50);
		a11.setText("1");
		a12.setMaxSize(50,50);
		a12.setText("0");
		a21.setMaxSize(50,50);
		a21.setText("0");
		a22.setMaxSize(50,50);
		a22.setText("1");
		matrixPane.add(matrix, 0, 0, 2, 1);
		matrixPane.add(a11, 0, 1);
		matrixPane.add(a12, 1, 1);
		matrixPane.add(a21, 0, 2);
		matrixPane.add(a22, 1, 2);
		matrixPane.add(button, 0, 3, 2, 1);
		matrixPane.setVgap(20);
		matrixPane.setHgap(20);
		GridPane.setHalignment(matrix, HPos.CENTER);
		GridPane.setHalignment(button, HPos.CENTER);
		planePane.getChildren().addAll(xAxis, yAxis, line1, line2, line3, line4, max, M1, M2, negM1, negM2);
		max.setX(0);
		max.setY(AXIS_START);
		M1.setX(ORIGIN - 5);
		M1.setY(AXIS_START - 5);
		M2.setX(AXIS_START + AXIS_LENGTH + 5);
		M2.setY(ORIGIN + 5);
		negM1.setX(ORIGIN - 6);
		negM1.setY(AXIS_START + AXIS_LENGTH + 12);
		negM2.setX(AXIS_START - 18);
		negM2.setY(ORIGIN + 4);
		xAxis.setStartX(AXIS_START);
		xAxis.setStartY(AXIS_START + AXIS_LENGTH / 2);
		xAxis.endXProperty().bind(xAxis.startXProperty().add(AXIS_LENGTH));
		xAxis.endYProperty().bind(xAxis.startYProperty());
		yAxis.setStartX(AXIS_START + AXIS_LENGTH / 2);
		yAxis.setStartY(AXIS_START);
		yAxis.endXProperty().bind(yAxis.startXProperty());
		yAxis.endYProperty().bind(yAxis.startYProperty().add(AXIS_LENGTH));
		redraw(planePane, line1, line2, line3, line4, 
				new double[] {x1.get(),y1.get()}, 
				new double[] {x2.get(),y2.get()}, 
				new double[] {x3.get(),y3.get()}, 
				new double[] {x4.get(),y4.get()}, 
				maxBounds);
		line1.setStroke(Color.RED);
		line2.setStroke(Color.BLUE);
		line3.setStroke(Color.GREEN);
		planePane.setPrefSize(600,600);
		masterPane.setLeft(matrixPane);
		masterPane.setRight(planePane);
		masterPane.setPadding(new Insets(10,10,10,10));
		
		button.setOnAction(e -> {
			try {
				double newA11 = Double.parseDouble(a11.getText());
				double newA12 = Double.parseDouble(a12.getText());
				double newA21 = Double.parseDouble(a21.getText());
				double newA22 = Double.parseDouble(a22.getText());
				
				double[][] newPoints = transform(new Matrix(new double[][] {{newA11, newA12},{newA21, newA22}}));
//				String debug = "" + maxBounds;
//				masterPane.setBottom(new Text(debug));
				
				double oldBounds = maxBounds;
				
				for (double[] point: newPoints) {
					double maxCoord = Vector.maxCoord(point);
					if (maxCoord > maxBounds || maxCoord < maxBounds / 4.0)
						maxBounds = maxCoord * 2;
				}
				
				redraw(planePane, line1, line2, line3, line4, 
						newPoints[0], 
						newPoints[1], 
						newPoints[2], 
						newPoints[3], 
						maxBounds);
				
				if (maxBounds != oldBounds) {
					max.setText("M = " + maxBounds);
				}
			}
			catch (NumberFormatException ex) {
				popup("Please only enter numbers.");
			}
		});
		
		Scene scene = new Scene(masterPane, 800, 600);
		primaryStage.setTitle("Matrix Visualizer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void redraw(Pane planePane, Line line1, Line line2, Line line3, Line line4, double[] point1, double[] point2, double[] point3, double[] point4, double maxBounds) {
		line1.setStartX(ORIGIN + (point1[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line1.setStartY(ORIGIN - (point1[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line1.setEndX(ORIGIN + (point2[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line1.setEndY(ORIGIN - (point2[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line2.setStartX(ORIGIN + (point2[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line2.setStartY(ORIGIN - (point2[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line2.setEndX(ORIGIN + (point3[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line2.setEndY(ORIGIN - (point3[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line3.setStartX(ORIGIN + (point3[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line3.setStartY(ORIGIN - (point3[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line3.setEndX(ORIGIN + (point4[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line3.setEndY(ORIGIN - (point4[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line4.setStartX(ORIGIN + (point4[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line4.setStartY(ORIGIN - (point4[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		line4.setEndX(ORIGIN + (point1[0] / maxBounds) * (AXIS_LENGTH / 2.0));
		line4.setEndY(ORIGIN - (point1[1] / maxBounds) * (AXIS_LENGTH / 2.0));
		
		
//		line1.startYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point1[1] / maxBounds) * (1.0/2)));
//		line1.endXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point2[0] / maxBounds) * (1.0/2)));
//		line1.endYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point2[1] / maxBounds) * (1.0/2)));
//		line2.startXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point2[0] / maxBounds) * (1.0/2)));
//		line2.startYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point2[1] / maxBounds) * (1.0/2)));
//		line2.endXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point3[0] / maxBounds) * (1.0/2)));
//		line2.endYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point3[1] / maxBounds) * (1.0/2)));
//		line3.startXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point3[0] / maxBounds) * (1.0/2)));
//		line3.startYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point3[1] / maxBounds) * (1.0/2)));
//		line3.endXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point4[0] / maxBounds) * (1.0/2)));
//		line3.endYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point4[1] / maxBounds) * (1.0/2)));
//		line4.startXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point4[0] / maxBounds) * (1.0/2)));
//		line4.startYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point4[1] / maxBounds) * (1.0/2)));
//		line4.endXProperty().bind(planePane.widthProperty().multiply((1.0/2) + (point1[0] / maxBounds) * (1.0/2)));
//		line4.endYProperty().bind(planePane.heightProperty().multiply((1.0/2) + (point1[1] / maxBounds) * (1.0/2)));
	}
	
	private double[][] transform(Matrix matrix) {
		double[][] newPoints = new double[4][2];
		double[][] oldPoints = {{1,1},{-1,1},{-1,-1},{1,-1}};
		
		for (int i = 0; i < newPoints.length; i++) {
			newPoints[i] = Vector.multiply(matrix, new Vector(oldPoints[i])).getVector();
		}
		
		return newPoints;
	}
	
	public void popup(String message) {
		Stage okStage = new Stage();
		GridPane okPane = new GridPane();
		Label toDisplay = new Label(message);
		Button button = new Button("OK");
		okPane.add(toDisplay,0,0);
		okPane.add(button, 0, 1);
		okPane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(toDisplay, HPos.CENTER);
		GridPane.setValignment(toDisplay, VPos.CENTER);
		GridPane.setHalignment(button, HPos.CENTER);
		GridPane.setValignment(button, VPos.CENTER);
		okPane.setVgap(20);
		button.setOnAction(e -> okStage.close());
		Scene okScene = new Scene(okPane, 250, 200);
		okStage.setTitle("Matrix Visualizer - Popup");
		okStage.setScene(okScene);
		okStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
