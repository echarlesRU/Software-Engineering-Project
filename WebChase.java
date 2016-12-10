/**
* License: CC Attribution Non-Commercial 4.0 International
*    See short hand summary here:
*        http://creativecommons.org/licenses/by-nc/4.0/
*    See legal specifications here:
*        http://creativecommons.org/licenses/by-nc/4.0/legalcode
*
*    Reuse of code allowed under the conditions in the link above.
*/
package webchase;

import java.util.*;
import java.util.concurrent.*;
import javafx.application.Application;

/**
 * WebChase Application v1.1
 * See README for more information.
 * @author John Filipowicz
 */
public class WebChase {

    /**
     * Launches the application
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Application.launch(PrimaryView.class, args);
    }
}
