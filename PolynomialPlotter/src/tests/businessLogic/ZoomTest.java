/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.businessLogic;

import java.io.IOException;
import java.util.logging.Logger;
import logic.BusinessLogic;
import logic.FunctionManager;
import logic.FunctionParsingException;
import logic.HornerParser;
import logic.IParser;
import model.Tuple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import startup.ISettings;

/**
 *
 * @author robinepple
 */
public class ZoomTest {
    
    private BusinessLogic bl;
    GuiMock view;

    @Before
    public void setUp() throws IOException, FunctionParsingException {
        // Objekte initialisieren
        ISettings settings = initSettings();
        FunctionManager model = new FunctionManager(new IParser[]{new HornerParser()});
        view = new GuiMock(settings);

        bl = new BusinessLogic(view, model, settings);
    }

    private ISettings initSettings() {
        var settings = new SettingsMock();
        settings.logger = Logger.getGlobal();
        settings.calculateEveryXPixels = 1;
        settings.initialOriginX = 0;
        settings.initialOriginY = 0;
        settings.initialPixelToUnitRatio = 1;
        settings.initialPlotHeight = 20;
        settings.initialPlotWidth = 20;
        settings.threadpoolSize = 8;
        // settings.theme = ;
        return settings;
    }

    @Test
    public void ZoomTest() throws FunctionParsingException {
        // Raus zoomen, Mausposition Mitte 1. Quadrant.
        bl.zoom(2, new Tuple<>(15, 15));
        var intervallX = view.drawingInformation.getIntervallX();
        var intervallY = view.drawingInformation.getIntervallX();
        Assert.assertEquals(-39, (double)intervallX.getItem1(), 0);
        Assert.assertEquals(21, (double)intervallX.getItem2(), 0);
        Assert.assertEquals(-39, (double)intervallY.getItem1(), 0);
        Assert.assertEquals(21, (double)intervallX.getItem2(), 0);
    }
}
