package controller.customTags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Business logic of my own custom tag.
 * Sends to jsp URI of requested view.
 *
 * @author JohnUkraine
 */
public class SelectedPageTagHandler extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        HttpServletRequest request = getRequest();
        sendViewUriToJsp(request);
    }

    /**
     * Get request from jspContext
     *
     * @return httpRequest
     */
    private HttpServletRequest getRequest() {
        PageContext pageContext = (PageContext) getJspContext();
        return (HttpServletRequest) pageContext.getRequest();
    }

    /**
     * Writes URI of current view to out stream of jspContext
     *
     * @param request
     * @throws IOException
     */
    private void sendViewUriToJsp(HttpServletRequest request)
            throws IOException {
        JspWriter out = getJspContext().getOut();
        out.print(request.getRequestURI());
    }
}
