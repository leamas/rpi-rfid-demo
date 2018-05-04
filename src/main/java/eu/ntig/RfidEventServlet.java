package eu.ntig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.liangyuen.pi4j_rc522.Block;
import com.liangyuen.pi4j_rc522.BlockAddress;
import com.liangyuen.pi4j_rc522.Key;
import com.liangyuen.pi4j_rc522.RC522Exception;
import com.liangyuen.pi4j_rc522.RaspRC522;
import com.liangyuen.pi4j_rc522.Uid;

public class RfidEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Key KEY_A = new Key("ff:ff:ff:ff:ff:ff");
    //private static Key KEY_B = new Key("ff:ff:ff:ff:ff:ff");

    private static Logger logger =
        LogManager.getLogger(RfidEventServlet.class.getName());

    protected Uid getUid(RaspRC522 rc522) throws InterruptedException
    {
        int back_bits;
        Uid uid = null;
        while (true) {
            Thread.sleep(2000);
            try {
                back_bits = rc522.setupTranscieve(RaspRC522.PICC_REQIDL);
                logger.info("Detected:"+back_bits);
            } catch (RC522Exception ex) {
                logger.debug("No card detected");
                return null;
            }
            uid = (rc522.antiColl());
            if (uid.isFailed()) {
                logger.debug("anticoll error");
                return null;
            }
            try {
                int size=rc522.selectTag(uid);
                logger.debug("Size="+size);
            }
            catch (RC522Exception ex) {
                logger.debug("Cannot select: " + ex.getMessage());
                return null;
            }
            logger.debug("New UID: " + uid.toString(","));
                return uid;
        }
    }

    public Block unlockAndRead(RaspRC522 rc522, Uid uid, BlockAddress address)
    {
        int status = 0;
        Block block = null;
        try {
            status = rc522.authCard(RaspRC522.PICC_AUTHENT1A, address,
                                    KEY_A, uid);
        }
        catch (RC522Exception ex) {
            logger.info("Cannot authenticate: %d", status);
            return null;
        }
        if (status != RaspRC522.MI_OK) {
            logger.info("Authenticate A error");
            return null;
        }
        try {
            block = rc522.read(new BlockAddress(0, 3));
            logger.info("Authenticated, read data: " + block.toString());
        }
        catch (RC522Exception ex) {
            logger.debug("Cannot authenticate");
            return null;
        }
        try {
            block = rc522.read(new BlockAddress(0, 3));
            logger.info("Read control block data: " + block.toString());
        }
        catch (RC522Exception ex) {
            logger.debug("Cannot read control block");
        }
        rc522.stopCrypto();
        return block;
    }

    public void eventStream(PrintWriter writer) throws InterruptedException
    {
        logger.info("Starting rfid event stream");
        writer.write("data: Initiate\n\n");
        RaspRC522 rc522 = new RaspRC522();
        Uid uid;
        BlockAddress address = new BlockAddress(15, 2);
        Block block;
        while (true) {
            Thread.sleep(2000);
            uid = getUid(rc522);
            if (uid == null)
                continue;
            logger.debug("New UID: " + uid.toString(","));
            writer.write("data: New UID: " + uid.toString(":") + "\n\n");
            block  = unlockAndRead(rc522, uid, address);
            if (block == null)
                continue;
            writer.write("data: " + block.toString() + "\n\n");
        }
    }

    protected void staticFileResponse(String uri, PrintWriter writer)
        throws IOException
    {
        InputStream istream = getServletContext().getResourceAsStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        String line = null;
        while ((line = reader.readLine()) != null) {
            writer.println(line);
        }

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        logger.trace("Entering doGet()");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        PrintWriter writer = resp.getWriter();
        logger.debug("URI: " + uri);
        if (uri.endsWith("/ui")) {
            resp.setContentType("text/html");
            staticFileResponse("/ui/index.html", writer);
        } else {
            resp.setContentType("text/event-stream");
            try {
                eventStream(resp.getWriter());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//public class ModHelloWorld extends HttpServlet{
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        response.sendRedirect("http://www.google.com");
//    }
//}
//
//https://stackoverflow.com/questions/17036130/how-to-return-an-html-document-from-java-servlet
