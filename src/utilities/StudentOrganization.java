package utilities;

import database.Database;
import users.student.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentOrganization implements Serializable {

    private String orgName;
    private final List<Student> members;
    private Student head;

    public StudentOrganization(String orgName, Student head) {
        this.orgName = orgName;
        this.head = head;
        this.members = new ArrayList<>();
        Database.getInstance().addStudentOrganization(this);
        this.addMember(head);
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<Student> getMembers() {
        return members;
    }

    public Student getHead() {
        return head;
    }

    public void setHead(Student head) {
        if (!members.contains(head)) {
            addMember(head);
        }
        this.head = head;
        System.out.println("The new head of the organization is " + head.getName());
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            System.out.println(student.getName() + " has joined the organization " + orgName);
        } else {
            System.out.println(student.getName() + " is already a member of " + orgName);
        }
    }

    public void removeMember(Student student) {
        if (members.contains(student)) {
            members.remove(student);
            System.out.println(student.getName() + " has left the organization " + orgName);
        } else {
            System.out.println(student.getName() + " is not a member of " + orgName);
        }
    }

    @Override
    public String toString() {
        return "Organization: " + orgName +
                "\nHead: " + (head != null ? head.getName() : "None") +
                "\nMembers: " + members.size();
    }

    public void displayDetails() {
        System.out.println("Organization: " + orgName);
        System.out.println("Head: " + (head != null ? head.getName() : "None"));
        System.out.println("Members:");
        for (Student member : members) {
            System.out.println("- " + member.getName());
        }
    }
}
