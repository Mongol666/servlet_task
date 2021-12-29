package servlets;

import group.GroupRepository;
import mark.MarkRepository;
import secondary.Group;
import secondary.Mark;
import secondary.Subject;
import subject.SubjectRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/MarksServlet")
public class MarksServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MarkRepository markRepository = (MarkRepository) getServletContext().getAttribute("mark_repository");

        int studentID = Integer.parseInt(req.getParameter("studentID"));
        int groupID = Integer.parseInt(req.getParameter("newGroupID"));
        int count = Integer.parseInt(req.getParameter("newMark"));
        LocalDate date = LocalDate.parse(req.getParameter("newDate"));
        int subjectID = Integer.parseInt(req.getParameter("newSubjectID"));

        markRepository.createMark(new Mark()
                .withGroupId(groupID)
                .withMark(count)
                .withDateOfMark(date)
                .withSubjectId(subjectID)
                .withStudentId(studentID));
        req.getRequestDispatcher("admin_students_marks.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MarkRepository markRepository = (MarkRepository) getServletContext().getAttribute("mark_repository");
        GroupRepository groupRepository = (GroupRepository) getServletContext().getAttribute("group_repository");
        SubjectRepository subjectRepository = (SubjectRepository) getServletContext().getAttribute("subject_repository");

        int markID = Integer.parseInt(req.getParameter("ID"));
        int groupID = Integer.parseInt(req.getParameter("newGroupID"));
        int count = Integer.parseInt(req.getParameter("newMark"));
        LocalDate date = LocalDate.parse(req.getParameter("newDate"));
        int subjectID = Integer.parseInt(req.getParameter("newSubjectID"));

        markRepository.updateDateOfMarkById(markID, date);

        Optional<Group> optionalGroup = groupRepository.getGroupById(groupID);
        optionalGroup.ifPresent(group -> markRepository.updateGroupWhereMarkWasGiven(markID, group));

        markRepository.updateMarkById(markID, count);

        Optional<Subject> subjectOptional = subjectRepository.getSubjectById(subjectID);
        subjectOptional.ifPresent(subject -> markRepository.updateSubjectMarkById(subjectID, subject));

        req.getRequestDispatcher("admin_students_marks.jsp").forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MarkRepository markRepository = (MarkRepository) getServletContext().getAttribute("mark_repository");
        int id = Integer.parseInt(req.getParameter("ID"));
        markRepository.deleteMarkById(id);
        req.getRequestDispatcher("admin_students_marks.jsp").forward(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getParameter("method")) {
            case "delete":
                doDelete(req, resp);
                break;
            case "put":
                doPut(req, resp);
                break;
            case "post":
                doPost(req, resp);
                break;
        }
    }
}