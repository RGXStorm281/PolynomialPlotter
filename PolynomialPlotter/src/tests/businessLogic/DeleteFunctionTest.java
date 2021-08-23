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
public class DeleteFunctionTest {

    private BusinessLogic bl;
    GuiMock view;
    private static final String TEST_FUNCTION_NAME = "test";
    private static final String TEST_FUNCTION = "5x^2+3x+2";

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
    public void DeleteFunctionTest() {
        bl.deleteFunction(TEST_FUNCTION_NAME);
        var functionInfo = view.drawingInformation.getFunctionInfo();
        Assert.assertEquals(0, functionInfo.length);
    }
}
