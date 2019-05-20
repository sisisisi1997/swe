package hu.sisisisi.szamforgato.views;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashMap;

class ViewUtilities
{
    private static HashMap<Button, ButtonData> buttonData = new HashMap<>();

    static void showButtonErrorText(Button b, String errorText)
    {
        ButtonData manipulationData = buttonData.get(b);
        if(manipulationData == null)
        {
            buttonData.put(b, new ButtonData(b));
        }

        b.setTextFill(Color.RED);
        b.setText(errorText);
        buttonData.get(b).resetErrorThread();
    }

    private static class ButtonData
    {
        private String originalText;
        private Paint originalColour;
        private Thread errorThread;
        private Runnable errorRunnable;

        void resetErrorThread()
        {
            if(this.errorThread != null && this.errorThread.isAlive())
            {
                this.errorThread.interrupt();
            }
            this.errorThread = new Thread(this.errorRunnable);
            this.errorThread.start();
        }

        ButtonData(final Button button)
        {
            this.originalText = button.getText();
            this.originalColour = button.getTextFill();
            this.errorRunnable = () ->
            {
                try
                {
                    Thread.sleep(1500);
                    Platform.runLater(() ->
                    {
                        button.setText(this.originalText);
                        button.setTextFill(this.originalColour);
                    });
                }
                catch(InterruptedException ex)
                {
                    // resetelve lett az idő addig, amíg visszaállítjuk
                    // nincs teendőnk a catchben
                }
            };
        }

        @Override
        public boolean equals(Object other)
        {
            if(!(other instanceof ButtonData))
                return false;

            ButtonData b = (ButtonData)other;
            return this.originalText.equals(b.originalText) && this.originalColour.equals(b.originalColour);
        }

        @Override
        public int hashCode()
        {
            return super.hashCode() * originalText.hashCode() * originalColour.hashCode();
        }
    }
}
