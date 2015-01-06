package de.eisfeldj.augendiagnosefx.util;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import de.eisfeldj.augendiagnosefx.controller.Controller;
import de.eisfeldj.augendiagnosefx.controller.MainController;
import de.eisfeldj.augendiagnosefx.controller.MenuController;

/**
 * Utility class for reading FXML files.
 */
public final class FXMLUtil {

	/**
	 * The mainController of the main application.
	 */
	private static MainController mainController = null;

	/**
	 * The menu controller.
	 */
	private static MenuController menuController = null;

	/**
	 * Private constructor to prevent instantiation.
	 */
	private FXMLUtil() {
		// do nothing
	}

	/**
	 * Utility method to expand an FXML file, including internationalization.
	 *
	 * The controller is added to the controller registry.
	 *
	 * @param fxmlFile
	 *            The name of the FXML file.
	 * @return The controller handling the expanded page.
	 * @throws IOException
	 */
	public static Controller getRootFromFxml(final String fxmlFile) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(ResourceUtil.STRINGS_BUNDLE);
		Parent root = fxmlLoader.load(ClassLoader.getSystemResource("fxml/" + fxmlFile).openStream());
		root.getStylesheets().add(ClassLoader.getSystemResource("css/application.css").toExternalForm());

		if (fxmlLoader.getController() instanceof MainController) {
			FXMLUtil.mainController = (MainController) fxmlLoader.getController();
		}
		else if (fxmlLoader.getController() instanceof MenuController) {
			FXMLUtil.menuController = (MenuController) fxmlLoader.getController();
			FXMLUtil.menuController.setMainController(FXMLUtil.mainController);
		}

		return fxmlLoader.getController();
	}

	/**
	 * Utility method to expand and display an FXML file in the body as subpage.
	 *
	 * @param fxmlFile
	 *            The name of the FXML file.
	 * @return the controller of the subpage.
	 * @throws IOException
	 */
	public static Controller displaySubpage(final String fxmlFile) {
		Controller controller;
		try {
			controller = getRootFromFxml(fxmlFile);
		}
		catch (IOException e) {
			Logger.error("Failed to load FXML file " + fxmlFile, e);
			return null;
		}
		mainController.addSubPage(controller);

		// Enable close menu
		final EventHandler<ActionEvent> closeHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				removeSubpage(controller);
			}
		};
		menuController.enableClose(closeHandler);

		return controller;
	}

	/**
	 * Utility method to remove a node. In particular, a full pane can be removed.
	 *
	 * @param node
	 *            The node to be removed.
	 */
	public static void remove(final Node node) {
		if (node.getParent() != null && node.getParent() instanceof Pane) {
			((Pane) node.getParent()).getChildren().remove(node);
		}
	}

	/**
	 * Utility method to remove a pane from the stack.
	 *
	 * @param controller
	 *            The controller of the pane to be removed.
	 */
	public static void removeSubpage(final Controller controller) {
		mainController.removeSubPage(controller);
		menuController.disableClose();
	}

	/**
	 * Utility method to remove all panes from the stack.
	 */
	public static void removeAllSubpages() {
		mainController.removeAllSubPages();
		menuController.disableAllClose();
	}

	/**
	 * Utility method to expand and display the menu bar.
	 *
	 * @param fxmlFile
	 *            The name of the FXML file.
	 * @throws IOException
	 */
	public static void displayMenu(final String fxmlFile) throws IOException {
		MenuBar root = (MenuBar) getRootFromFxml(fxmlFile).getRoot();
		mainController.getMenuBar().getMenus().clear();
		mainController.getMenuBar().getMenus().addAll(root.getMenus());
	}

	/**
	 * Utility method to temporarily add a visible border around a region.
	 *
	 * @param region
	 *            The region getting the border.
	 * @param color
	 *            the color of the border.
	 */
	public static void addDummyBorder(final Region region, final Color color) {
		region.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, null, new BorderWidths(5)))); // MAGIC_NUMBER
	}

}
