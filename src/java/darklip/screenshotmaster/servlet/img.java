package darklip.screenshotmaster.servlet;

import darklip.screenshotmaster.data.ImageGetter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Darklip
 */
@WebServlet(name = "img", urlPatterns = {"/img"})
public class img extends HttpServlet {
    
    ImageGetter imgGetter = new ImageGetter();
    String imgName = "", imgFormat = "", errorMsg = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        imgGetter.getImageFromDb(Integer.parseInt(request.getParameter("id")));
        imgName = imgGetter.getImgName();
        imgFormat = imgGetter.getImgFormat();
        errorMsg = imgGetter.getError();
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Screenshot Master</title>");
            out.println("</head>");
            out.println("<body>");
            if (errorMsg.equals("No errors")) {
                try {
                    String imgPath = "C:\\Users\\1\\server_images\\" + imgName + '.' + imgFormat;
                    BufferedImage bImage = ImageIO.read(new File(imgPath));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, imgFormat, baos);
                    baos.flush();
                    byte[] imageInByteArray = baos.toByteArray();
                    baos.close();
                    String b64 = DatatypeConverter.printBase64Binary(imageInByteArray);
                    out.println("<img src=\"data:image/" + imgFormat + ";base64, " + b64 + "\"/>");
                } catch (IOException e) {
                    out.println("Error: " + e);
                }
            } else {
                out.println("Error: " + errorMsg);
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet returns an image page and makes a short url.";
    }// </editor-fold>

}
