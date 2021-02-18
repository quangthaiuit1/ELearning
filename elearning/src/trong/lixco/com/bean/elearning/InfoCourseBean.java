package trong.lixco.com.bean.elearning;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.ejb.service.elearning.SkillDetailService;
import trong.lixco.com.ejb.service.elearning.StoragePathService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.SkillDetail;
import trong.lixco.com.util.PDFMerger;

@Named
@ViewScoped
public class InfoCourseBean extends AbstractBean<Course> {

	private static final long serialVersionUID = 1L;
	private List<SkillDetail> skillDetailsBySkill;
	private SkillDetail skillDetailSelected;
	private String pathVideo = "";
	@Inject
	private StoragePathService STORAGE_PATH_SERVICE;
	@Inject
	private SkillDetailService SKILL_DETAIL_SERVICE;

	@Override
	protected void initItem() {
		long pdSkillSelectedId = getPlanDetailId();
		skillDetailsBySkill = SKILL_DETAIL_SERVICE.findBySkill(pdSkillSelectedId);
		if (!skillDetailsBySkill.isEmpty()) {
			skillDetailSelected = skillDetailsBySkill.get(0);
			pathVideo = skillDetailSelected.getFile_video();
		}
	}

	public long getPlanDetailId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("pdsid");
		if (setofIdTemp == null || setofIdTemp.equals("null")) {
			return 0;
		}
		return Long.parseLong(setofIdTemp);
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public void skillDetailOnRowSelect() {
		pathVideo = skillDetailSelected.getFile_video();
	}

	public void processDataAssign() {
		if (skillDetailSelected != null) {
			byte[] file = PDFMerger.getFile(STORAGE_PATH_SERVICE.findByName("file").getPath(),
					this.skillDetailSelected.getFile_document());
			if (file != null) {
				PrimeFaces.current().executeScript("document.getElementById('formdataassign:process').click();");
			} else {
				notice("Không có dữ liệu chứng minh.");
			}
		} else {
			notice("Lưu dữ liệu trước khi nạp dữ liệu chứng minh.");
		}
	}

	public void showPDFData() throws IOException {
		if (skillDetailSelected != null && skillDetailSelected.getId() != null) {
			try {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				try {
					byte[] file = PDFMerger.getFile(STORAGE_PATH_SERVICE.findByName("file").getPath(),
							skillDetailSelected.getFile_document());
					HttpSession session = (HttpSession) externalContext.getSession(true);
					HttpServletRequest ht = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
							.getRequest();
					ITextRenderer renderer = new ITextRenderer();
					String url = "http://" + ht.getServerName() + ":" + ht.getServerPort()
							+ "/elearning/showdata.xhtml;jsessionid=" + session.getId() + "?pdf=true";
					renderer.setDocument(new URL(url).toString());
					renderer.layout();

					HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
					response.setContentType("application/pdf");
					response.addHeader("Content-disposition", "inline;filename=report.pdf");

					response.setContentLength(file.length);
					response.getOutputStream().write(file, 0, file.length);
					response.getOutputStream().flush();

					OutputStream browserStream = response.getOutputStream();
					renderer.createPDF(browserStream);
				} finally {
					facesContext.responseComplete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	int count = 0;
	// bat dau tinh gio video
	Thread saveTimeElapsedVideo = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				System.out.println("Does it work?");
				System.out.println("Thai " + count);
				count += 1;
				Thread.sleep(30000);
				saveTimeElapsedVideo.run();

			} catch (InterruptedException v) {
				System.out.println(v);
			}
		}
	});

	// test video
	private String events = "";

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	public void onPlay() {
		events = "play" + "\n" + events;
		// saveTimeElapsedVideo.start();
		System.out.println("da vao play");
	}

	public void onPause() {
		events = "pause" + "\n" + events;
		// saveTimeElapsedVideo.stop();
	}

	public void onSeeking() {
		events = "seeking" + "\n" + events;
	}

	public void onCanplaythrough() {
		events = "can play through" + "\n" + events;
	}

	public void onLoadeddata() {
		events = "loaded data" + "\n" + events;
	}

	public void onLoadeddataNew() {
		events = "loaded data" + "\n" + events;
	}

	public List<SkillDetail> getSkillDetailsBySkill() {
		return skillDetailsBySkill;
	}

	public void setSkillDetailsBySkill(List<SkillDetail> skillDetailsBySkill) {
		this.skillDetailsBySkill = skillDetailsBySkill;
	}

	public SkillDetail getSkillDetailSelected() {
		return skillDetailSelected;
	}

	public void setSkillDetailSelected(SkillDetail skillDetailSelected) {
		this.skillDetailSelected = skillDetailSelected;
	}

	public String getPathVideo() {
		return pathVideo;
	}

	public void setPathVideo(String pathVideo) {
		this.pathVideo = pathVideo;
	}

}
