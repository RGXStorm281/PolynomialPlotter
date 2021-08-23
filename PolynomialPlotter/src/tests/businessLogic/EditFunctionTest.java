/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.businessLogic;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Logger;
import logic.BusinessLogic;
import logic.FunctionManager;
import logic.FunctionParsingException;
import logic.HornerParser;
import logic.IParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import model.ISettings;

/**
 *
 * @author robinepple
 */
public class EditFunctionTest {
    
    private BusinessLogic bl;
    GuiMock view;
    private static final String TEST_FUNCTION_NAME = "test";
    private static final String TEST_FUNCTION = "5x^2+3x+2";
    private static final String TEST_FUNCTION_NAME_EDITED = "test_edited";
    private static final String TEST_FUNCTION_EDITED = "3x^2";

    @Before
    public void setUp() throws IOException, FunctionParsingException {
        // Objekte initialisieren
        ISettings settings = initSettings();
        FunctionManager model = new FunctionManager(new IParser[]{new HornerParser()});
        view = new GuiMock(settings);

        bl = new BusinessLogic(view, model, settings);
        bl.addFunction(TEST_FUNCTION_NAME, TEST_FUNCTION, Color.BLUE);
    }

    private ISettings initSettings() {
        var settings = new SettingsMock();
        settings.logger = Logger.getGlobal();
        settings.calculateEveryXPixels = 1;
        settings.initialOriginX = 0;
        settings.initialOriginY = 0;
        settings.initialPixelToUnitRatio = 1;
        settings.initialPlotHeight = 10;
        settings.initialPlotWidth = 10;
        settings.threadpoolSize = 8;
        // settings.theme = ;
        return settings;
    }

    @Test
    public void EditFunctionTest() throws FunctionParsingException {
        bl.editFunction(TEST_FUNCTION_NAME, TEST_FUNCTION_NAME_EDITED, TEST_FUNCTION_EDITED, Color.red);
        var functionInfo = view.drawingInformation.getFunctionInfo();
        var function = functionInfo[0].getFunction();
        Assert.assertEquals(TEST_FUNCTION_NAME_EDITED, function.getFunctionName());
        Assert.assertEquals(TEST_FUNCTION_EDITED, function.getDisplayString());
        Assert.assertEquals(Color.red, function.getColor());
    }
}
