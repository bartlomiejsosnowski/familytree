package pl.ssn.familytree.pdfanalyzer.person;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private final Integer id;

	private final Sex sex;

	private final String firstName;

	private final String lastName;

	private final String details;

	private final String maidenName;

	private final String town;

	private final Integer yearOfBirth;

	private final String index;

	private String fatherName;

	private String motherName;

	private String motherMaidenName;

	private Person mother;

	private Person father;

	private Person partner;

	private List<Person> children;

	public Person(Integer id, Sex sex, String firstName, String lastName, String details, String maidenName,
			String town, Integer yearOfBirth, String index) {
		this.id = id;
		if (sex != null) {
			this.sex = sex;
		} else {
			this.sex = Sex.UNKNOWN;
		}
		this.firstName = firstName;
		this.lastName = lastName;
		this.details = details;
		this.maidenName = maidenName;
		this.town = town;
		this.yearOfBirth = yearOfBirth;
		this.index = index;
	}

	public Person(Integer id, Sex sex, String firstName, String lastName, String details, String maidenName,
			String town, Integer yearOfBirth, String index, String fatherName, String motherName,
			String motherMaidenName) {
		this(id, sex, firstName, lastName, details, motherMaidenName, town, yearOfBirth, index);
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.motherMaidenName = motherMaidenName;
	}

	public Integer getId() {
		return id;
	}

	public Sex getSex() {
		return sex;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public String getDetails() {
		return details;
	}

	public String getTown() {
		return town;
	}

	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

	public String getIndex() {
		return index;
	}

	public Person getMother() {
		return mother;
	}

	public Person getFather() {
		return father;
	}

	public String getFatherName() {
		return fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public String getMotherMaidenName() {
		return motherMaidenName;
	}

	public List<Person> getChildren() {
		return children;
	}

	public void addChild(Person child) {
		if (this.children == null) {
			this.children = new ArrayList<Person>();
		}
		if (!children.contains(child)) {
			this.children.add(child);
			if (this.sex == Sex.FEMALE) {
				child.mother = this;
				child.motherName = this.firstName;
				child.motherMaidenName = this.maidenName;
			} else if (this.sex == Sex.MALE) {
				child.father = this;
				child.fatherName = this.firstName;
			}
		}
	}

	public void setMother(Person mother) {
		this.mother = mother;
		if (mother != null) {
			mother.addChild(this);
		}
	}

	public void setFather(Person father) {
		this.father = father;
		if (father != null) {
			father.addChild(this);
		}
	}

	public Person getPartner() {
		return partner;
	}

	public void setPartner(Person person) {
		this.partner = person;
		if (person != null) {
			person.partner = this;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fatherName == null) ? 0 : fatherName.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((motherMaidenName == null) ? 0 : motherMaidenName.hashCode());
		result = prime * result + ((motherName == null) ? 0 : motherName.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (sex != other.sex)
			return false;
		if (firstName != null && other.firstName != null && !firstName.equals(other.firstName))
			return false;
		if (lastName != null && other.lastName != null && !lastName.equals(other.lastName))
			return false;
		if (fatherName != null && other.fatherName != null && !fatherName.equals(other.fatherName))
			return false;
		if (motherMaidenName != null && other.motherMaidenName != null
				&& !motherMaidenName.equals(other.motherMaidenName))
			return false;
		if (motherName != null && other.motherName != null && !motherName.equals(other.motherName))
			return false;
		if (yearOfBirth != null && other.yearOfBirth != null
				&& (yearOfBirth >= other.yearOfBirth + 2 || yearOfBirth <= other.yearOfBirth - 2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Person [");
		str.append("id=\"").append(id).append("\"");
		str.append(", sex=\"").append(sex).append("\"");
		str.append(", firstName=\"").append(firstName).append("\"");
		str.append(", lastName=\"").append(lastName).append("\"");
		str.append(", town=\"").append(town).append("\"");
		str.append(", details=\"").append(details).append("\"");
		str.append(", maidenName=\"").append(maidenName).append("\"");
		str.append(", yearOfBirth=\"").append(yearOfBirth).append("\"");
		str.append(", index=\"").append(index).append("\"");
		str.append(", fatherName=\"").append(fatherName).append("\"");
		str.append(", motherName=\"").append(motherName).append("\"");
		str.append(", motherMaidenName=\"").append(motherMaidenName).append("\"");
		if (partner != null)
			str.append(", partner=\"").append(partner.getFirstName()).append(' ').append(partner.getLastName())
					.append("\"");
		if (father != null)
			str.append(", father=\"").append(father.getFirstName()).append(' ').append(father.getLastName())
					.append("\"");
		if (mother != null)
			str.append(", mother=\"").append(mother.getFirstName()).append(' ').append(mother.getLastName())
					.append("\"");
		if (children != null) {
			str.append(", children=\"");
			for (Person child : children) {
				str.append(child.getFirstName()).append(' ').append(child.getLastName());
			}
			str.append("\"");
		}
		str.append("]");
		return str.toString();
	}

}
