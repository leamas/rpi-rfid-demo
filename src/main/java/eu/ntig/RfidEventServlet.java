package eu.ntig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RfidEventServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger =
        LogManager.getLogger(RfidEventServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {

        logger.warn("Entering doget()");
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        if (uri.endsWith("/ui")) {
            RequestDispatcher view = req.getRequestDispatcher("ui/index.html");
            view.forward(req, resp);
        } else {
            // Get rfid events...
        }

        Socket rfidSocket = new Socket("localhost", 1111);
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(rfidSocket.getInputStream()));
        PrintWriter writer = resp.getWriter();

        String event;
        while ((event = reader.readLine()) != null) {
            writer.write(event + "\n\n");
            writer.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        writer.close();
        rfidSocket.close();
    }
}

//public class ModHelloWorld extends HttpServlet{
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        response.sendRedirect("http://www.google.com");
//    }
//}
//
//https://stackoverflow.com/questions/17036130/how-to-return-an-html-document-from-java-servlet
