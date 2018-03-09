package eu.ntig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RfidEventServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		Socket rfidSocket = new Socket("localhost", 1111);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(rfidSocket.getInputStream()));
		PrintWriter writer = response.getWriter();
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
