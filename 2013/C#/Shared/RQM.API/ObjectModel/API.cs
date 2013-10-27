using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Xml;
using System.Xml.Schema;
using System.Xml.Serialization;

namespace RQM.API.ObjectModel
{

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplan")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testplan")]
	public class TestPlan : ReportableArtifact
	{
		[XmlElement("com.ibm.rqm.planning.editor.section.planSamplingStrategy")]
		public XmlNode SamplingStrategy;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestEquipment")]
		public XmlNode TestEquipment;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestCondition")]
		public XmlNode TestCondition;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestStrategy")]
		public XmlNode TestStrategy;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestData")]
		public XmlNode TestData;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestBed")]
		public XmlNode TestBed;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestIdentification")]
		public XmlNode TestIdentification;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestEnvDetails")]
		public XmlNode TestEnvDetails;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestObjectives")]
		public XmlNode TestObjectives;

		[XmlElement("com.ibm.rqm.planning.editor.section.planBusinessObjectives")]
		public XmlNode BusinessObjectives;

		[XmlElement("com.ibm.rqm.planning.editor.section.planTestScope")]
		public XmlNode TestScope;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlArray(ElementName = "approvals")]
		[XmlArrayItem("approvalDescriptor", IsNullable = false)]
		public List<ApprovalRequest> Approvals;

		[XmlElement(ElementName = "risk")]
		public Risk Risk;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "alias")]
		public string Alias;

		[XmlElement("alias1")]
		public List<Alias> Alias1;

		[XmlArray(ElementName = "objectives")]
		[XmlArrayItem("objectiveStatus", IsNullable = false)]
		public List<TestplanObjectiveStatus> Objectives;

		[XmlArray(ElementName = "keydates")]
		[XmlArrayItem("keydate", IsNullable = false)]
		public List<TestplanKeydate> Keydates;

		[XmlElement(ElementName = "estimation")]
		public Estimation Estimation;

		[XmlElement("category")]
		public List<TestPlan_Category> Category;

		[XmlElement("testcase")]
		public List<HrefAndID> Testcase;

		[XmlElement("testsuite")]
		public List<HrefAndID> Testsuite;

		[XmlElement("childplan")]
		public List<HrefAndID> Childplan;

		[XmlElement(ElementName = "template")]
		public HrefAndID Template;

		[XmlElement("requirement")]
		public List<TestplanRequirement> Requirement;

		[XmlElement("requirementset")]
		public List<TestplanRequirementset> Requirementset;

		[XmlElement("requirementView")]
		public List<TestplanRequirementView> RequirementView;

		[XmlElement("requirementPackage")]
		public List<TestplanRequirementPackage> RequirementPackage;

		[XmlElement("developmentplan")]
		public List<TestplanDevelopmentplan> Developmentplan;

		[XmlElement("workitem")]
		public List<TestplanWorkitem> Workitem;

		[XmlElement(ElementName = "teamarea")]
		public HrefAndID Teamarea;

		[XmlElement("configuration")]
		public List<HrefAndID> Configuration;

		[XmlElement("attachment")]
		public List<HrefAndID> Attachment;

		[XmlArray(ElementName = "platformcoverage")]
		[XmlArrayItem("catalogType", IsNullable = false)]
		public List<TestplanCatalogType> Platformcoverage;

		[XmlElement("testphase")]
		public List<TestplanTestphase> Testphase;

		[XmlElement(ElementName = "defectTurnaround")]
		public string DefectTurnaround;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeApprovals()
		{
			return ((this.Approvals != null)
						&& (this.Approvals.Count > 0));
		}

		public virtual bool ShouldSerializeObjectives()
		{
			return ((this.Objectives != null)
						&& (this.Objectives.Count > 0));
		}

		public virtual bool ShouldSerializeKeydates()
		{
			return ((this.Keydates != null)
						&& (this.Keydates.Count > 0));
		}

		public virtual bool ShouldSerializePlatformcoverage()
		{
			return ((this.Platformcoverage != null)
						&& (this.Platformcoverage.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://purl.org/dc/elements/1.1/", TypeName = "creator")]
	[XmlRoot(ElementName = "creator")]
	public class Creator
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public string Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "reportableArtifact")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = true, ElementName = "reportableArtifact")]
	public class ReportableArtifact
	{

		[XmlElement(ElementName = "projectArea")]
		public HrefAndID ProjectArea;
	}

	[Serializable]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/")]
	public class HrefAndID
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string HREF;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/v0.1/", TypeName = "owner")]
	[XmlRoot(ElementName = "owner")]
	public class Owner
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public string Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "approvalsApprovalDescriptor")]
	[XmlRoot(ElementName = "approvalsApprovalDescriptor")]
	public class ApprovalRequest
	{

		[XmlElement(ElementName = "approvalType")]
		public string ApprovalType;

		[XmlElement(ElementName = "approvalDate")]
		public DateTime ApprovalDate;

		[XmlIgnore]
		public bool ApprovalDateSpecified;

		[XmlElement("approval")]
		public List<ApprovalRequest_Approval> Approval;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "approvalsApprovalDescriptorApproval")]
	[XmlRoot(ElementName = "approvalsApprovalDescriptorApproval")]
	public class ApprovalRequest_Approval
	{

		[XmlElement(ElementName = "approvalOwner")]
		public ApprovalRequest_Approval_Approver ApprovalOwner;

		[XmlElement(ElementName = "approvalStatus")]
		public string ApprovalStatus;

		[XmlElement(ElementName = "comment")]
		public string Comment;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "approvalsApprovalDescriptorApprovalApprovalOwner")]
	[XmlRoot(ElementName = "approvalsApprovalDescriptorApprovalApprovalOwner")]
	public class ApprovalRequest_Approval_Approver
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "risk")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "risk")]
	public class Risk
	{

		[XmlElement(ElementName = "id")]
		public int Id;

		[XmlIgnore]
		public bool IdSpecified;

		[XmlElement(ElementName = "riskLevel")]
		public int RiskLevel;

		[XmlIgnore]
		public bool RiskLevelSpecified;

		[XmlArray(ElementName = "factors")]
		[XmlArrayItem("factor", IsNullable = false)]
		public List<RiskFactor> Factors;

		[XmlElement(ElementName = "commnunityAssessment")]
		public RiskCommnunityAssessment CommnunityAssessment;

		public virtual bool ShouldSerializeFactors()
		{
			return ((this.Factors != null)
						&& (this.Factors.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "riskFactor")]
	[XmlRoot(ElementName = "riskFactor")]
	public class RiskFactor
	{

		[XmlElement(ElementName = "id")]
		public int Id;

		[XmlIgnore]
		public bool IdSpecified;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "cost")]
		public int Cost;

		[XmlIgnore]
		public bool CostSpecified;

		[XmlElement(ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlElement(ElementName = "probability")]
		public int Probability;

		[XmlIgnore]
		public bool ProbabilitySpecified;

		[XmlElement(ElementName = "mitigationAction")]
		public string MitigationAction;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "riskCommnunityAssessment")]
	[XmlRoot(ElementName = "riskCommnunityAssessment")]
	public class RiskCommnunityAssessment
	{

		[XmlElement(ElementName = "riskLevel")]
		public int RiskLevel;

		[XmlIgnore]
		public bool RiskLevelSpecified;

		[XmlElement(ElementName = "veryHighAmount")]
		public int VeryHighAmount;

		[XmlIgnore]
		public bool VeryHighAmountSpecified;

		[XmlElement(ElementName = "highAmount")]
		public int HighAmount;

		[XmlIgnore]
		public bool HighAmountSpecified;

		[XmlElement(ElementName = "neutralAmount")]
		public int NeutralAmount;

		[XmlIgnore]
		public bool NeutralAmountSpecified;

		[XmlElement(ElementName = "lowAmount")]
		public int LowAmount;

		[XmlIgnore]
		public bool LowAmountSpecified;

		[XmlElement(ElementName = "veryLowAmount")]
		public int VeryLowAmount;

		[XmlIgnore]
		public bool VeryLowAmountSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "alias")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "alias")]
	public class Alias
	{

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlText]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanObjectiveStatus")]
	[XmlRoot(ElementName = "testplanObjectiveStatus")]
	public class TestplanObjectiveStatus
	{

		[XmlElement(ElementName = "id")]
		public int Id;

		[XmlIgnore]
		public bool IdSpecified;

		[XmlElement(ElementName = "group")]
		public string Group;

		[XmlElement(ElementName = "groupOrder")]
		public int GroupOrder;

		[XmlIgnore]
		public bool GroupOrderSpecified;

		[XmlElement(ElementName = "actualValue")]
		public int ActualValue;

		[XmlIgnore]
		public bool ActualValueSpecified;

		[XmlElement(ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "comment")]
		public string Comment;

		[XmlElement(ElementName = "objective")]
		public HrefAndID Objective;

		[XmlElement(ElementName = "objectiveStateId")]
		public string ObjectiveStateId;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanKeydate")]
	[XmlRoot(ElementName = "testplanKeydate")]
	public class TestplanKeydate
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "date")]
		public DateTime Date;

		[XmlIgnore]
		public bool DateSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "estimation")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "estimation")]
	public class Estimation
	{

		[XmlElement(ElementName = "planningEffort")]
		public int PlanningEffort;

		[XmlIgnore]
		public bool PlanningEffortSpecified;

		[XmlElement(ElementName = "executionEffort")]
		public int ExecutionEffort;

		[XmlIgnore]
		public bool ExecutionEffortSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanCategory")]
	[XmlRoot(ElementName = "testplanCategory")]
	public class TestPlan_Category
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}
	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanRequirement")]
	[XmlRoot(ElementName = "testplanRequirement")]
	public class TestplanRequirement
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "externalReqId")]
		public string ExternalReqId;

		[XmlAttribute(AttributeName = "reqProId")]
		public string ReqProId;

		[XmlAttribute(AttributeName = "reqProProject")]
		public string ReqProProject;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanRequirementset")]
	[XmlRoot(ElementName = "testplanRequirementset")]
	public class TestplanRequirementset
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanRequirementView")]
	[XmlRoot(ElementName = "testplanRequirementView")]
	public class TestplanRequirementView
	{

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "description")]
		public string Description;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanRequirementPackage")]
	[XmlRoot(ElementName = "testplanRequirementPackage")]
	public class TestplanRequirementPackage
	{

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "description")]
		public string Description;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanDevelopmentplan")]
	[XmlRoot(ElementName = "testplanDevelopmentplan")]
	public class TestplanDevelopmentplan
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanWorkitem")]
	[XmlRoot(ElementName = "testplanWorkitem")]
	public class TestplanWorkitem
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanCatalogType")]
	[XmlRoot(ElementName = "testplanCatalogType")]
	public class TestplanCatalogType
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "partname")]
		public TestplanCatalogTypePartname Partname;

		[XmlElement(ElementName = "id")]
		public string Id;

		[XmlElement("path")]
		public List<TestplanCatalogTypePath> Path;

		[XmlElement(ElementName = "template")]
		public TestplanCatalogTypeTemplate Template;

		[XmlElement("values")]
		public List<TestplanCatalogTypeValues> Values;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanCatalogTypePartname")]
	[XmlRoot(ElementName = "testplanCatalogTypePartname")]
	public class TestplanCatalogTypePartname
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanCatalogTypePath")]
	[XmlRoot(ElementName = "testplanCatalogTypePath")]
	public class TestplanCatalogTypePath
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanCatalogTypeTemplate")]
	[XmlRoot(ElementName = "testplanCatalogTypeTemplate")]
	public class TestplanCatalogTypeTemplate
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanCatalogTypeValues")]
	[XmlRoot(ElementName = "testplanCatalogTypeValues")]
	public class TestplanCatalogTypeValues
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testplanTestphase")]
	[XmlRoot(ElementName = "testplanTestphase")]
	public class TestplanTestphase
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "customAttributesCustomAttribute")]
	[XmlRoot(ElementName = "customAttributesCustomAttribute")]
	public class CustomAttributesCustomAttribute
	{

		[XmlElement(ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "value")]
		public string Value;

		[XmlElement(ElementName = "description")]
		public string Description;

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(AttributeName = "required")]
		public bool Required;

		[XmlIgnore]
		public bool RequiredSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "category")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "category")]
	public class Category : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "categoryType")]
		public CategoryCategoryType CategoryType;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryCategoryType")]
	[XmlRoot(ElementName = "categoryCategoryType")]
	public class CategoryCategoryType
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryType")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "categoryType")]
	public class CategoryType : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "scope")]
		public string Scope;

		[XmlElement(ElementName = "required")]
		public bool Required;

		[XmlIgnore]
		public bool RequiredSpecified;

		[XmlElement(ElementName = "multiSelectable")]
		public bool MultiSelectable;

		[XmlIgnore]
		public bool MultiSelectableSpecified;

		[XmlElement(ElementName = "dependsOn")]
		public CategoryTypeDependsOn DependsOn;

		[XmlElement(ElementName = "defaultCategory")]
		public CategoryTypeDefaultCategory DefaultCategory;

		[XmlElement("valueset")]
		public List<CategoryTypeValueset> Valueset;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryTypeDependsOn")]
	[XmlRoot(ElementName = "categoryTypeDependsOn")]
	public class CategoryTypeDependsOn
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryTypeDefaultCategory")]
	[XmlRoot(ElementName = "categoryTypeDefaultCategory")]
	public class CategoryTypeDefaultCategory
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryTypeValueset")]
	[XmlRoot(ElementName = "categoryTypeValueset")]
	public class CategoryTypeValueset
	{

		[XmlElement(ElementName = "key")]
		public CategoryTypeValuesetKey Key;

		[XmlElement("value")]
		public List<CategoryTypeValuesetValue> Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryTypeValuesetKey")]
	[XmlRoot(ElementName = "categoryTypeValuesetKey")]
	public class CategoryTypeValuesetKey
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "categoryTypeValuesetValue")]
	[XmlRoot(ElementName = "categoryTypeValuesetValue")]
	public class CategoryTypeValuesetValue
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testphase")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testphase")]
	public class TestPhase : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "expectedTotalPoints")]
		public int ExpectedTotalPoints;

		[XmlIgnore]
		public bool ExpectedTotalPointsSpecified;

		[XmlElement(ElementName = "expectedDefects")]
		public int ExpectedDefects;

		[XmlIgnore]
		public bool ExpectedDefectsSpecified;

		[XmlElement(ElementName = "expectedValidityRate")]
		public int ExpectedValidityRate;

		[XmlIgnore]
		public bool ExpectedValidityRateSpecified;

		[XmlElement(ElementName = "expectedStartDate")]
		public DateTime ExpectedStartDate;

		[XmlElement(ElementName = "expectedEndDate")]
		public DateTime ExpectedEndDate;

		[XmlElement(ElementName = "starttime")]
		public DateTime Starttime;

		[XmlElement(ElementName = "endtime")]
		public DateTime Endtime;

		[XmlElement(ElementName = "custom1")]
		public string Custom1;

		[XmlElement(ElementName = "custom2")]
		public string Custom2;

		[XmlElement(ElementName = "custom3")]
		public string Custom3;

		[XmlElement(ElementName = "testplan")]
		public HrefAndID TestPlan;

		[XmlElement("detail")]
		public List<TestphaseDetail> Detail;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testphaseDetail")]
	[XmlRoot(ElementName = "testphaseDetail")]
	public class TestphaseDetail
	{

		[XmlElement(ElementName = "comment")]
		public string Comment;

		[XmlElement(ElementName = "date")]
		public DateTime Date;

		[XmlElement(ElementName = "pointsAttempted")]
		public int PointsAttempted;

		[XmlIgnore]
		public bool PointsAttemptedSpecified;

		[XmlElement(ElementName = "pointsCompleted")]
		public int PointsCompleted;

		[XmlIgnore]
		public bool PointsCompletedSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testcase")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testcase")]
	public class TestCase : ReportableArtifact
	{
		[XmlElement("com.ibm.rqm.planning.editor.section.testCaseExpectedResults")]
		public XmlNode ExpectedResults;

		[XmlElement("com.ibm.rqm.planning.editor.section.testCasePostCondition")]
		public XmlNode PostCondition;

		[XmlElement("com.ibm.rqm.planning.editor.section.testCasePreCondition")]
		public XmlNode PreCondition;

		[XmlElement("com.ibm.rqm.planning.editor.section.caseTestDescription")]
		public XmlNode TestCaseDescription;

		[XmlElement("com.ibm.rqm.planning.editor.section.caseTestPreparation")]
		public XmlNode Preparation;

		[XmlElement("com.ibm.rqm.planning.editor.section.caseTestScope")]
		public XmlNode Scope;

		[XmlElement("com.ibm.rqm.planning.editor.section.testCaseNotes")]
		public XmlNode Notes;

		[XmlElement("com.ibm.rqm.planning.editor.section.testCaseDesign")]
		public XmlNode Design;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement("alias")]
		public List<Alias> Alias;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlElement(ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlElement(ElementName = "trigger")]
		public string Trigger;

		[XmlElement(ElementName = "activity")]
		public string Activity;

		[XmlArray(ElementName = "approvals")]
		[XmlArrayItem("approvalDescriptor", IsNullable = false)]
		public List<ApprovalRequest> Approvals;

		[XmlElement(ElementName = "risk")]
		public Risk Risk;

		[XmlElement("category")]
		public List<TestcaseCategory> Categories;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlElement("testscript")]
		public List<HrefAndID> TestScripts;

		[XmlElement("remotescript")]
		public List<HrefAndID> Remotescript;

		[XmlElement(ElementName = "template")]
		public HrefAndID Template;

		[XmlElement("requirement")]
		public List<TestcaseRequirement> Requirement;

		[XmlElement("workitem")]
		public List<TestcaseWorkitem> Workitem;

		[XmlElement("attachment")]
		public List<HrefAndID> Attachment;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeApprovals()
		{
			return ((this.Approvals != null)
						&& (this.Approvals.Count > 0));
		}

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testcaseCategory")]
	[XmlRoot(ElementName = "testcaseCategory")]
	public class TestcaseCategory
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "variablesVariable")]
	[XmlRoot(ElementName = "variablesVariable")]
	public class VariablesVariable
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "value")]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testcaseRequirement")]
	[XmlRoot(ElementName = "testcaseRequirement")]
	public class TestcaseRequirement
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "externalReqId")]
		public string ExternalReqId;

		[XmlAttribute(AttributeName = "reqProId")]
		public string ReqProId;

		[XmlAttribute(AttributeName = "reqProProject")]
		public string ReqProProject;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testcaseWorkitem")]
	[XmlRoot(ElementName = "testcaseWorkitem")]
	public class TestcaseWorkitem
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testscript")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testscript")]
	public class TestScript : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement("alias")]
		public List<Alias> Alias;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlElement(ElementName = "scripttype")]
		public string Scripttype;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlArray(ElementName = "steps")]
		[XmlArrayItem("step", Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", IsNullable = false)]
		public List<Step> Steps;

		[XmlElement(ElementName = "datapool")]
		public TestscriptDatapool Datapool;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement("category")]
		public List<TestscriptCategory> Category;

		[XmlElement("workitem")]
		public List<TestscriptWorkitem> Workitem;

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeSteps()
		{
			return ((this.Steps != null)
						&& (this.Steps.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", TypeName = "step")]
	[XmlRoot(ElementName = "step")]
	public class Step
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "description")]
		public StepDescription Description;

		[XmlElement(ElementName = "expectedResult")]
		public StepExpectedResult ExpectedResult;

		[XmlElement(ElementName = "comment")]
		public string Comment;

		[XmlElement(ElementName = "compare")]
		public string Compare;

		[XmlElement("property")]
		public List<StepProperty> Property;

		[XmlElement("attachment")]
		public List<StepAttachment> Attachment;

		[XmlElement(ElementName = "link")]
		public StepLink Link;

		[XmlElement("step1")]
		public List<Step> Step1;

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", TypeName = "stepDescription")]
	[XmlRoot(ElementName = "stepDescription")]
	public class StepDescription
	{

		[XmlText]
		[XmlAnyElement(Namespace = "http://www.w3.org/1999/xhtml")]
		public List<XmlNode> Any;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", TypeName = "stepExpectedResult")]
	[XmlRoot(ElementName = "stepExpectedResult")]
	public class StepExpectedResult
	{

		[XmlText]
		[XmlAnyElement(Namespace = "http://www.w3.org/1999/xhtml")]
		public List<XmlNode> Any;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", TypeName = "stepProperty")]
	[XmlRoot(ElementName = "stepProperty")]
	public class StepProperty
	{

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlText]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", TypeName = "stepAttachment")]
	[XmlRoot(ElementName = "stepAttachment")]
	public class StepAttachment
	{

		[XmlAttribute(AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/testscript/v0.1/", TypeName = "stepLink")]
	[XmlRoot(ElementName = "stepLink")]
	public class StepLink
	{

		[XmlAttribute(AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testscriptDatapool")]
	[XmlRoot(ElementName = "testscriptDatapool")]
	public class TestscriptDatapool
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testscriptCategory")]
	[XmlRoot(ElementName = "testscriptCategory")]
	public class TestscriptCategory
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testscriptWorkitem")]
	[XmlRoot(ElementName = "testscriptWorkitem")]
	public class TestscriptWorkitem
	{

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "remotescript")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "remotescript")]
	public class RemoteScript : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement("alias")]
		public List<Alias> Alias;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlElement(ElementName = "tags")]
		public string Tags;

		[XmlElement(ElementName = "type")]
		public string Type;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlElement(ElementName = "shareprefix")]
		public string Shareprefix;

		[XmlElement(ElementName = "relativepath")]
		public string Relativepath;

		[XmlElement(ElementName = "manageadapter")]
		public bool Manageadapter;

		[XmlIgnore]
		public bool ManageadapterSpecified;

		[XmlElement(ElementName = "adapterid")]
		public string Adapterid;

		[XmlElement(ElementName = "fullpath")]
		public string Fullpath;

		[XmlElement(ElementName = "authorid")]
		public RemotescriptAuthorid Authorid;

		[XmlElement(ElementName = "ownerid")]
		public RemotescriptOwnerid Ownerid;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement("category")]
		public List<RemotescriptCategory> Category;

		[XmlElement("workitem")]
		public List<RemotescriptWorkitem> Workitem;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "remotescriptAuthorid")]
	[XmlRoot(ElementName = "remotescriptAuthorid")]
	public class RemotescriptAuthorid
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "remotescriptOwnerid")]
	[XmlRoot(ElementName = "remotescriptOwnerid")]
	public class RemotescriptOwnerid
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "remotescriptCategory")]
	[XmlRoot(ElementName = "remotescriptCategory")]
	public class RemotescriptCategory
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "remotescriptWorkitem")]
	[XmlRoot(ElementName = "remotescriptWorkitem")]
	public class RemotescriptWorkitem
	{

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "keyword")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "keyword")]
	public class Keyword : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "tags")]
		public string Tags;

		[XmlElement("testscript")]
		public List<HrefAndID> Testscript;

		[XmlElement("remotescript")]
		public List<HrefAndID> Remotescript;

		[XmlElement(ElementName = "defaultscript")]
		public HrefAndID Defaultscript;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "datapool")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "datapool")]
	public class Datapool : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "attachment")]
		public DatapoolAttachment Attachment;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<DatapoolVariable> Variables;

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "datapoolAttachment")]
	[XmlRoot(ElementName = "datapoolAttachment")]
	public class DatapoolAttachment
	{

		[XmlAttribute(AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "datapoolVariable")]
	[XmlRoot(ElementName = "datapoolVariable")]
	public class DatapoolVariable
	{

		[XmlElement(ElementName = "description")]
		public DatapoolVariableDescription Description;

		[XmlAttribute(AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(AttributeName = "role")]
		public int Role;

		[XmlIgnore]
		public bool RoleSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "datapoolVariableDescription")]
	[XmlRoot(ElementName = "datapoolVariableDescription")]
	public class DatapoolVariableDescription
	{

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlText]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuite")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testsuite")]
	public class TestSuite : ReportableArtifact
	{
		[XmlElement("com.ibm.rqm.planning.editor.section.testSuitePostCondition")]
		public XmlNode PostCondition;

		[XmlElement("comibmrqmexecutioneditorsectiondesign")]
		public XmlNode Design;

		[XmlElement("com.ibm.rqm.planning.editor.section.testSuiteExpectedResults")]
		public XmlNode ExpectedResults;

		[XmlElement("com.ibm.rqm.planning.editor.section.dynamicSection_1311012366866")]
		public XmlNode DynamicSection_1311012366866;

		[XmlElement("com.ibm.rqm.planning.editor.section.testSuitePreCondition")]
		public XmlNode PreCondition;

		[XmlElement("com.ibm.rqm.planning.editor.section.dynamicSection_1311012198706")]
		public XmlNode DynamicSection_1311012198706;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement("alias")]
		public List<Alias> Alias;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "sequentialExecution")]
		public bool SequentialExecution;

		[XmlIgnore]
		public bool SequentialExecutionSpecified;

		[XmlElement(ElementName = "passVariables")]
		public bool PassVariables;

		[XmlIgnore]
		public bool PassVariablesSpecified;

		[XmlElement(ElementName = "haltOnFailure")]
		public bool HaltOnFailure;

		[XmlIgnore]
		public bool HaltOnFailureSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlElement(ElementName = "authorid")]
		public TestSuiteAuthorID Authorid;

		[XmlElement(ElementName = "ownerid")]
		public TestSuiteOwnerID Ownerid;

		[XmlArray(ElementName = "approvals")]
		[XmlArrayItem("approvalDescriptor", IsNullable = false)]
		public List<ApprovalRequest> Approvals;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlElement(ElementName = "risk")]
		public Risk Risk;

		[XmlElement(ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlArray(ElementName = "suiteelements")]
		[XmlArrayItem("suiteelement", IsNullable = false)]
		public List<SuiteelementsSuiteelement> Suiteelements;

		[XmlElement("category")]
		public List<TestsuiteCategory> Category;

		[XmlElement("testplan")]
		public List<TestsuiteTestplan> Testplan;

		[XmlElement(ElementName = "template")]
		public HrefAndID Template;

		[XmlElement("attachment")]
		public List<HrefAndID> Attachment;

		[XmlElement("workitem")]
		public List<TestsuiteWorkitem> Workitem;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeApprovals()
		{
			return ((this.Approvals != null)
						&& (this.Approvals.Count > 0));
		}

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeSuiteelements()
		{
			return ((this.Suiteelements != null)
						&& (this.Suiteelements.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuiteAuthorid")]
	[XmlRoot(ElementName = "testsuiteAuthorid")]
	public class TestSuiteAuthorID
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuiteOwnerid")]
	[XmlRoot(ElementName = "testsuiteOwnerid")]
	public class TestSuiteOwnerID
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "suiteelementsSuiteelement")]
	[XmlRoot(ElementName = "suiteelementsSuiteelement")]
	public class SuiteelementsSuiteelement
	{

		[XmlElement(ElementName = "testcase")]
		public HrefAndID Testcase;

		[XmlElement(ElementName = "configuration")]
		public HrefAndID Configuration;

		[XmlElement("remotescript", typeof(SuiteelementsSuiteelementRemotescript))]
		[XmlElement("testscript", typeof(SuiteelementsSuiteelementTestscript))]
		public object Item;

		[XmlAttribute(AttributeName = "elementindex")]
		public int Elementindex;

		[XmlIgnore]
		public bool ElementindexSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "SuiteelementsSuiteelementRemotescript")]
	[XmlRoot(ElementName = "SuiteelementsSuiteelementRemotescript")]
	public class SuiteelementsSuiteelementRemotescript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "SuiteelementsSuiteelementTestscript")]
	[XmlRoot(ElementName = "SuiteelementsSuiteelementTestscript")]
	public class SuiteelementsSuiteelementTestscript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuiteCategory")]
	[XmlRoot(ElementName = "testsuiteCategory")]
	public class TestsuiteCategory
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuiteTestplan")]
	[XmlRoot(ElementName = "testsuiteTestplan")]
	public class TestsuiteTestplan
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "relation")]
		public string Relation;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuiteWorkitem")]
	[XmlRoot(ElementName = "testsuiteWorkitem")]
	public class TestsuiteWorkitem
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testsuitelog")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testsuitelog")]
	public class TestSuiteLog : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlElement(ElementName = "sequentialExecution")]
		public bool SequentialExecution;

		[XmlIgnore]
		public bool SequentialExecutionSpecified;

		[XmlElement(ElementName = "passVariables")]
		public bool PassVariables;

		[XmlIgnore]
		public bool PassVariablesSpecified;

		[XmlElement(ElementName = "haltOnFailure")]
		public bool HaltOnFailure;

		[XmlIgnore]
		public bool HaltOnFailureSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "pointspassed")]
		public int Pointspassed;

		[XmlIgnore]
		public bool PointspassedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "pointsfailed")]
		public int Pointsfailed;

		[XmlIgnore]
		public bool PointsfailedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "pointsattempted")]
		public int Pointsattempted;

		[XmlIgnore]
		public bool PointsattemptedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "pointsblocked")]
		public int Pointsblocked;

		[XmlIgnore]
		public bool PointsblockedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "pointsinconclusive")]
		public int Pointsinconclusive;

		[XmlIgnore]
		public bool PointsinconclusiveSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "starttime")]
		public DateTime Starttime;

		[XmlIgnore]
		public bool StarttimeSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "endtime")]
		public DateTime Endtime;

		[XmlIgnore]
		public bool EndtimeSpecified;

		[XmlArray(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", ElementName = "suiteelements")]
		[XmlArrayItem("suiteelement", IsNullable = false)]
		public List<SuiteelementsSuiteelement1> Suiteelements;

		[XmlElement(ElementName = "stateid")]
		public string Stateid;

		[XmlElement("executionresult")]
		public List<HrefAndID> Executionresult;

		[XmlElement("tasks")]
		public List<HrefAndID> Tasks;

		[XmlElement(ElementName = "suiteexecutionrecord")]
		public HrefAndID Suiteexecutionrecord;

		[XmlElement(ElementName = "testsuite")]
		public HrefAndID Testsuite;

		[XmlElement(ElementName = "testplan")]
		public HrefAndID Testplan;

		[XmlElement(ElementName = "testphase")]
		public HrefAndID Testphase;

		[XmlElement(ElementName = "buildrecord")]
		public HrefAndID Buildrecord;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeSuiteelements()
		{
			return ((this.Suiteelements != null)
						&& (this.Suiteelements.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", TypeName = "suiteelementsSuiteelement1")]
	[XmlRoot(ElementName = "suiteelementsSuiteelement1")]
	public class SuiteelementsSuiteelement1
	{

		[XmlElement(ElementName = "index")]
		public int Index;

		[XmlIgnore]
		public bool IndexSpecified;

		[XmlElement(ElementName = "adapterid")]
		public string Adapterid;

		[XmlElement(ElementName = "executionworkitem")]
		public SuiteelementsSuiteelementExecutionworkitem Executionworkitem;

		[XmlElement("remotescript", typeof(SuiteelementsSuiteelementRemotescript1))]
		[XmlElement("testscript", typeof(SuiteelementsSuiteelementTestscript1))]
		public object Item;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", TypeName = "suiteelementsSuiteelementExecutionworkitem")]
	[XmlRoot(ElementName = "suiteelementsSuiteelementExecutionworkitem")]
	public class SuiteelementsSuiteelementExecutionworkitem
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", TypeName = "suiteelementsSuiteelementRemotescript1")]
	[XmlRoot(ElementName = "suiteelementsSuiteelementRemotescript1")]
	public class SuiteelementsSuiteelementRemotescript1
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/tsl/v0.1/", TypeName = "suiteelementsSuiteelementTestscript1")]
	[XmlRoot(ElementName = "suiteelementsSuiteelementTestscript1")]
	public class SuiteelementsSuiteelementTestscript1
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionworkitem")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "executionworkitem")]
	public class ExecutionWorkItem : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "frequency")]
		public string Frequency;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "regression")]
		public bool Regression;

		[XmlIgnore]
		public bool RegressionSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "priority")]
		public string Priority;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "tags")]
		public string Tags;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "legacydata")]
		public string LegacyData;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionworkitem/v0.1", ElementName = "notes")]
		public string Notes;

		[XmlElement(ElementName = "risk")]
		public Risk Risk;

		[XmlElement(ElementName = "testcase")]
		public HrefAndID TestCase;

		[XmlElement("remotescript", typeof(ExecutionWorkItem_RemoteScript))]
		[XmlElement("testscript", typeof(ExecutionWorkItem_TestScript))]
		public object Item;

		[XmlElement(ElementName = "testplan")]
		public HrefAndID TestPlan;

		[XmlElement(ElementName = "testphase")]
		public HrefAndID TestPhase;

		[XmlElement(ElementName = "phase")]
		public ExecutionWorkItem_Phase Phase;

		[XmlElement("configuration")]
		public List<HrefAndID> Configuration;

		[XmlElement(ElementName = "currentexecutionresult")]
		public HrefAndID CurrentExecutionResult;

		[XmlElement("executionresult")]
		public List<HrefAndID> ExecutionResult;

		[XmlElement("blockingdefects")]
		public List<ExecutionworkitemBlockingdefects> BlockingDefects;

		[XmlElement("category")]
		public List<ExecutionworkitemCategory> Category;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutionworkitemRemotescript")]
	[XmlRoot(ElementName = "ExecutionworkitemRemotescript")]
	public class ExecutionWorkItem_RemoteScript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutionworkitemTestscript")]
	[XmlRoot(ElementName = "ExecutionworkitemTestscript")]
	public class ExecutionWorkItem_TestScript : HrefAndID { }


	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionworkitemPhase")]
	[XmlRoot(ElementName = "executionworkitemPhase")]
	public class ExecutionWorkItem_Phase
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionworkitemBlockingdefects")]
	[XmlRoot(ElementName = "executionworkitemBlockingdefects")]
	public class ExecutionworkitemBlockingdefects
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionworkitemCategory")]
	[XmlRoot(ElementName = "executionworkitemCategory")]
	public class ExecutionworkitemCategory
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "suiteexecutionrecord")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "suiteexecutionrecord")]
	public class SuiteExecutionRecord : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlElement(ElementName = "testplan")]
		public HrefAndID Testplan;

		[XmlElement(ElementName = "testphase")]
		public HrefAndID Testphase;

		[XmlElement(ElementName = "testsuite")]
		public HrefAndID Testsuite;

		[XmlElement(ElementName = "configuration")]
		public HrefAndID Configuration;

		[XmlElement(ElementName = "currenttestsuitelog")]
		public SuiteexecutionrecordCurrenttestsuitelog Currenttestsuitelog;

		[XmlElement("testsuitelog")]
		public List<SuiteexecutionrecordTestsuitelog> Testsuitelog;

		[XmlElement("category")]
		public List<SuiteexecutionrecordCategory> Category;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "suiteexecutionrecordCurrenttestsuitelog")]
	[XmlRoot(ElementName = "suiteexecutionrecordCurrenttestsuitelog")]
	public class SuiteexecutionrecordCurrenttestsuitelog
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "relation")]
		public string Relation;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "suiteexecutionrecordTestsuitelog")]
	[XmlRoot(ElementName = "suiteexecutionrecordTestsuitelog")]
	public class SuiteexecutionrecordTestsuitelog
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "relation")]
		public string Relation;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "suiteexecutionrecordCategory")]
	[XmlRoot(ElementName = "suiteexecutionrecordCategory")]
	public class SuiteexecutionrecordCategory
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionresult")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "executionresult")]
	public class ExecutionResult : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "creationDate")]
		public DateTime CreationDate;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "locked")]
		public bool Locked;

		[XmlIgnore]
		public bool LockedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "approvalstate")]
		public string ApprovalState;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "machine")]
		public string Machine;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "iterations")]
		public int Iterations;

		[XmlIgnore]
		public bool IterationsSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "buildid")]
		public string Buildid;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "log")]
		public string Log;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "starttime")]
		public DateTime StartTime;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "endtime")]
		public DateTime EndTime;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "weight")]
		public int Weight;

		[XmlIgnore]
		public bool WeightSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "pointspassed")]
		public int PointsPassed;

		[XmlIgnore]
		public bool PointsPassedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "pointsfailed")]
		public int PointsFailed;

		[XmlIgnore]
		public bool PointsFailedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "pointsattempted")]
		public int PointsAttempted;

		[XmlIgnore]
		public bool PointsAttemptedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "pointsblocked")]
		public int PointsBlocked;

		[XmlIgnore]
		public bool PointsBlockedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "pointsinconclusive")]
		public int PointsInconclusive;

		[XmlIgnore]
		public bool PointsInconclusiveSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "remoteexecution")]
		public bool RemoteExecution;

		[XmlIgnore]
		public bool RemoteExecutionSpecified;

		[XmlElement(ElementName = "stateid")]
		public string StateID;

		[XmlElement(ElementName = "itemid")]
		public string ItemID;

		[XmlElement(ElementName = "pointspermfailed")]
		public int PointsPermFailed;

		[XmlIgnore]
		public bool PointsPermFailedSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "details")]
		public Details Details;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "resultItemId")]
		public string ResultItemID;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "stepResultItemId")]
		public string StepResultItemID;

		[XmlElement(ElementName = "adapterId")]
		public string AdapterID;

		[XmlArray(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", ElementName = "stepResults")]
		[XmlArrayItem("stepResult", IsNullable = false)]
		public List<StepResult> StepResults;

		[XmlElement(ElementName = "testcase")]
		public HrefAndID Testcase;

		[XmlElement(ElementName = "testsuitelog")]
		public ExecutionResult_TestSuiteLog TestSuiteLog;

		[XmlElement("remotescript", typeof(ExecutionResult_RemoteScript))]
		[XmlElement("testscript", typeof(ExecutionResult_TestScript))]
		public object Item;

		[XmlElement(ElementName = "labresource")]
		public HrefAndID LabResource;

		[XmlElement(ElementName = "executionworkitem")]
		public HrefAndID ExecutionWorkItem;

		[XmlElement(ElementName = "testphase")]
		public HrefAndID TestPhase;

		[XmlElement(ElementName = "buildrecord")]
		public HrefAndID BuildRecord;

		[XmlElement(ElementName = "datapool")]
		public HrefAndID Datapool;

		[XmlElement("defect")]
		public List<ExecutionresultDefect> Defect;

		[XmlElement("attachment")]
		public List<HrefAndID> Attachment;

		[XmlElement(ElementName = "testplan")]
		public HrefAndID TestPlan;

		[XmlElement("alias")]
		public List<Alias> Alias;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement("category")]
		public List<ExecutionResult_Category> Category;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeStepResults()
		{
			return ((this.StepResults != null)
						&& (this.StepResults.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutionresultRemotescript")]
	[XmlRoot(ElementName = "ExecutionresultRemotescript")]
	public class ExecutionResult_RemoteScript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutionresultTestscript")]
	[XmlRoot(ElementName = "ExecutionresultTestscript")]
	public class ExecutionResult_TestScript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "details")]
	[XmlRoot(ElementName = "details")]
	public class Details
	{
		[XmlText]
		[XmlAnyElement(Namespace = "http://www.w3.org/1999/xhtml")]
		public List<XmlNode> Any;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResult")]
	[XmlRoot(ElementName = "stepResult")]
	public class StepResult
	{

		[XmlArray(ElementName = "properties")]
		[XmlArrayItem("property", IsNullable = false)]
		public List<StepResultProperty> Properties;

		[XmlElement(ElementName = "startTime")]
		public DateTime StartTime;

		[XmlIgnore]
		public bool StartTimeSpecified;

		[XmlElement(ElementName = "endTime")]
		public DateTime EndTime;

		[XmlIgnore]
		public bool EndTimeSpecified;

		[XmlElement(ElementName = "result")]
		public string Result;

		[XmlElement(ElementName = "expectedResult")]
		public StepResultExpectedResult ExpectedResult;

		[XmlElement(ElementName = "actualResult")]
		public StepResultActualResult ActualResult;

		[XmlElement(ElementName = "description")]
		public StepResultDescription Description;

		[XmlElement(ElementName = "stepType")]
		public string StepType;

		[XmlElement(ElementName = "comment")]
		public string Comment;

		[XmlElement(ElementName = "compare")]
		public string Compare;

		[XmlElement("stepAttachment")]
		public List<StepResultStepAttachment> StepAttachment;

		[XmlArray(ElementName = "stepResults")]
		[XmlArrayItem("stepResult", IsNullable = false)]
		public List<StepResult> StepResults;

		[XmlElement("defect")]
		public List<StepResultDefect> Defect;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAnyAttribute]
		public List<XmlAttribute> AnyAttr;

		public virtual bool ShouldSerializeProperties()
		{
			return ((Properties != null)
						&& (Properties.Count > 0));
		}

		public virtual bool ShouldSerializeStepResults()
		{
			return ((StepResults != null)
						&& (StepResults.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResultProperty")]
	[XmlRoot(ElementName = "stepResultProperty")]
	public class StepResultProperty
	{

		[XmlAttribute(AttributeName = "propertyName")]
		public string PropertyName;

		[XmlAttribute(AttributeName = "propertyValue")]
		public string PropertyValue;

		[XmlAttribute(AttributeName = "propertyType")]
		public string PropertyType;

		[XmlText]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResultExpectedResult")]
	[XmlRoot(ElementName = "stepResultExpectedResult")]
	public class StepResultExpectedResult
	{

		[XmlText]
		[XmlAnyElement(Namespace = "http://www.w3.org/1999/xhtml")]
		public List<XmlNode> Any;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResultActualResult")]
	[XmlRoot(ElementName = "stepResultActualResult")]
	public class StepResultActualResult
	{

		[XmlText]
		[XmlAnyElement(Namespace = "http://www.w3.org/1999/xhtml")]
		public List<XmlNode> Any;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResultDescription")]
	[XmlRoot(ElementName = "stepResultDescription")]
	public class StepResultDescription
	{

		[XmlText]
		[XmlAnyElement(Namespace = "http://www.w3.org/1999/xhtml")]
		public List<XmlNode> Any;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResultStepAttachment")]
	[XmlRoot(ElementName = "stepResultStepAttachment")]
	public class StepResultStepAttachment : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/executionresult/v0.1", TypeName = "stepResultDefect")]
	[XmlRoot(ElementName = "stepResultDefect")]
	public class StepResultDefect
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionresultTestsuitelog")]
	[XmlRoot(ElementName = "executionresultTestsuitelog")]
	public class ExecutionResult_TestSuiteLog
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "elementindex")]
		public int Elementindex;

		[XmlIgnore]
		public bool ElementindexSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionresultDefect")]
	[XmlRoot(ElementName = "executionresultDefect")]
	public class ExecutionresultDefect
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "rel")]
		public string Rel;

		[XmlAttribute(AttributeName = "summary")]
		public string Summary;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionresultCategory")]
	[XmlRoot(ElementName = "executionresultCategory")]
	public class ExecutionResult_Category
	{

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "template")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "template")]
	public class Template : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlArray(ElementName = "sections")]
		[XmlArrayItem("section", IsNullable = false)]
		public List<TemplateSection> Sections;

		public virtual bool ShouldSerializeSections()
		{
			return ((this.Sections != null)
						&& (this.Sections.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "templateSection")]
	[XmlRoot(ElementName = "templateSection")]
	public class TemplateSection
	{

		[XmlAttribute(AttributeName = "description")]
		public string Description;

		[XmlAttribute(AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(AttributeName = "configurationData")]
		public string ConfigurationData;

		[XmlAttribute(AttributeName = "name")]
		public string Name;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "objective")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "objective")]
	public class Objective : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(ElementName = "operator")]
		public string Operator;

		[XmlElement(ElementName = "targetValue")]
		public int TargetValue;

		[XmlIgnore]
		public bool TargetValueSpecified;

		[XmlElement(ElementName = "evaluatorId")]
		public string EvaluatorId;

		[XmlArray(ElementName = "properties")]
		[XmlArrayItem("property", IsNullable = false)]
		public List<ObjectiveProperty> Properties;

		public virtual bool ShouldSerializeProperties()
		{
			return ((this.Properties != null)
						&& (this.Properties.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "objectiveProperty")]
	[XmlRoot(ElementName = "objectiveProperty")]
	public class ObjectiveProperty
	{

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "value")]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "requirement")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "requirement")]
	public class Requirement : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "priority")]
		public string Priority;

		[XmlElement(ElementName = "reviewStatus")]
		public string ReviewStatus;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "suspectStatus")]
		public string SuspectStatus;

		[XmlElement(ElementName = "creationtime")]
		public DateTime Creationtime;

		[XmlElement(ElementName = "externalTag")]
		public string ExternalTag;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		[XmlElement(ElementName = "risk")]
		public Risk Risk;

		[XmlElement(ElementName = "tags")]
		public string Tags;

		[XmlElement(ElementName = "externalReqId")]
		public string ExternalReqId;

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "configuration")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "configuration")]
	public class Configuration : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "description")]
		public XmlNode Description;

		[XmlElement(ElementName = "summary")]
		public string Summary;

		[XmlElement("configuration")]
		public string Config;

		[XmlArray(ElementName = "configurationParts")]
		[XmlArrayItem("configurationPart", IsNullable = false)]
		public List<ConfigurationPart> ConfigurationParts;

		[XmlElement("comibmrationaltestlmconfiglrdid")]
		public List<com_ibm_rational_test_lm_config_lrdid> com_ibm_rational_test_lm_config_lrdid;

		public virtual bool ShouldSerializeConfigurationParts()
		{
			return ((this.ConfigurationParts != null)
						&& (this.ConfigurationParts.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "configurationPart")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "configurationPart")]
	public class ConfigurationPart
	{

		[XmlArray(ElementName = "attributes")]
		[XmlArrayItem("attribute", IsNullable = false)]
		public List<ConfigurationPartAttribute> Attributes;

		[XmlAttribute(DataType = "anyURI", AttributeName = "type")]
		public string Type;

		public virtual bool ShouldSerializeAttributes()
		{
			return ((this.Attributes != null)
						&& (this.Attributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "configurationPartAttribute")]
	[XmlRoot(ElementName = "configurationPartAttribute")]
	public class ConfigurationPartAttribute
	{

		[XmlElement("configurationPart", typeof(ConfigurationPart))]
		[XmlElement("value", typeof(ConfigurationPartAttributeValue))]
		public List<object> Items;

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(AttributeName = "pointerType")]
		public string PointerType;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "configurationPartAttributeValue")]
	[XmlRoot(ElementName = "configurationPartAttributeValue")]
	public class ConfigurationPartAttributeValue
	{

		[XmlAttribute(AttributeName = "displayName")]
		public string DisplayName;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "comibmrationaltestlmconfiglrdid")]
	[XmlRoot("comibmrationaltestlmconfiglrdid", Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false)]
	public class com_ibm_rational_test_lm_config_lrdid
	{

		[XmlAttribute(AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "request")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "request")]
	public class Request : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "confirmationDate")]
		public DateTime ConfirmationDate;

		[XmlElement(ElementName = "createDate")]
		public DateTime CreateDate;

		[XmlElement(ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "summary")]
		public string Summary;

		[XmlElement(ElementName = "fulfiller")]
		public RequestFulfiller Fulfiller;

		[XmlElement(ElementName = "requester")]
		public RequestRequester Requester;

		[XmlElement(ElementName = "reserveFrom")]
		public DateTime ReserveFrom;

		[XmlElement(ElementName = "reserveTo")]
		public DateTime ReserveTo;

		[XmlElement(ElementName = "requestType")]
		public string RequestType;

		[XmlElement(ElementName = "priority")]
		public string Priority;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "visibleId")]
		public string VisibleId;

		[XmlElement(ElementName = "configuration")]
		public string Configuration;

		[XmlElement(ElementName = "comments")]
		public string Comments;

		[XmlArray(ElementName = "changestates")]
		[XmlArrayItem("changestate", IsNullable = false)]
		public List<RequestChangestate> Changestates;

		public virtual bool ShouldSerializeChangestates()
		{
			return ((this.Changestates != null)
						&& (this.Changestates.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "requestFulfiller")]
	[XmlRoot(ElementName = "requestFulfiller")]
	public class RequestFulfiller
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "requestRequester")]
	[XmlRoot(ElementName = "requestRequester")]
	public class RequestRequester
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "requestChangestate")]
	[XmlRoot(ElementName = "requestChangestate")]
	public class RequestChangestate
	{

		[XmlElement(ElementName = "stateid")]
		public string Stateid;

		[XmlElement(ElementName = "oldstate")]
		public string Oldstate;

		[XmlElement(ElementName = "newstate")]
		public string Newstate;

		[XmlElement(ElementName = "statechangedate")]
		public DateTime Statechangedate;

		[XmlElement(ElementName = "oldstatechangedate")]
		public DateTime Oldstatechangedate;

		[XmlElement(ElementName = "statechangedby")]
		public RequestChangestateStatechangedby Statechangedby;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "requestChangestateStatechangedby")]
	[XmlRoot(ElementName = "requestChangestateStatechangedby")]
	public class RequestChangestateStatechangedby
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "reservation")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "reservation")]
	public class Reservation : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(ElementName = "reservedFor")]
		public string ReservedFor;

		[XmlElement(ElementName = "reservedBy")]
		public string ReservedBy;

		[XmlElement(ElementName = "reserveTo")]
		public DateTime ReserveTo;

		[XmlElement(ElementName = "reserveFrom")]
		public DateTime ReserveFrom;

		[XmlElement(ElementName = "visibleId")]
		public string VisibleId;

		[XmlElement(ElementName = "labresource")]
		public HrefAndID Labresource;

		[XmlElement(ElementName = "request")]
		public HrefAndID Request;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "resourcecollection")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "resourcecollection")]
	public class Resourcecollection
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "summary")]
		public string Summary;

		[XmlElement("labresource")]
		public List<ResourcecollectionLabresource> Labresource;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "resourcecollectionLabresource")]
	[XmlRoot(ElementName = "resourcecollectionLabresource")]
	public class ResourcecollectionLabresource
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "relation")]
		public string Relation;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testcell")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "testcell")]
	public class Testcell : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "type")]
		public int Type;

		[XmlIgnore]
		public bool TypeSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "longdescription")]
		public string Longdescription;

		[XmlElement(ElementName = "configuration")]
		public HrefAndID Configuration;

		[XmlElement("topology")]
		public List<TestcellTopology> Topology;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "testcellTopology")]
	[XmlRoot(ElementName = "testcellTopology")]
	public class TestcellTopology
	{

		[XmlElement(ElementName = "labresource")]
		public HrefAndID Labresource;

		[XmlElement("comibmrationaltestlmconfiglrdid")]
		public com_ibm_rational_test_lm_config_lrdid com_ibm_rational_test_lm_config_lrdid;

		[XmlElement(ElementName = "note")]
		public string Note;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "resourcegroup")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "resourcegroup")]
	public class Resourcegroup : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "isExpireable")]
		public bool IsExpireable;

		[XmlIgnore]
		public bool IsExpireableSpecified;

		[XmlElement(ElementName = "startDate")]
		public DateTime StartDate;

		[XmlElement(ElementName = "expirationdate")]
		public DateTime Expirationdate;

		[XmlElement(ElementName = "teamarea")]
		public HrefAndID Teamarea;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresource")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "labresource")]
	public class Labresource : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement("administrativeStatus", typeof(string))]
		[XmlElement("cpuArchitecture", typeof(string))]
		[XmlElement("cpuArchitectureWidth", typeof(string))]
		[XmlElement("cpuManufacturer", typeof(string))]
		[XmlElement("cpuSpeed", typeof(int))]
		[XmlElement("cpuType", typeof(LabresourceCpuType))]
		[XmlElement("diskspace", typeof(int))]
		[XmlElement("fullyQualifiedDomainName", typeof(string))]
		[XmlElement("hostname", typeof(string))]
		[XmlElement("imageType", typeof(string))]
		[XmlElement("ipAddress", typeof(string))]
		[XmlElement("memory", typeof(int))]
		[XmlElement("name", typeof(string))]
		[XmlElement("operationalStatus", typeof(string))]
		[XmlElement("osKernelWidth", typeof(string))]
		[XmlElement("osLocale", typeof(string))]
		[XmlElement("osType", typeof(LabresourceOsType))]
		[XmlElement("primaryMacAddress", typeof(string))]
		[XmlElement("resourcegroup", typeof(HrefAndID))]
		[XmlElement("software", typeof(LabresourceSoftware))]
		[XmlElement("systemId", typeof(string))]
		[XmlElement("tdm", typeof(string))]
		[XmlElement("type", typeof(LabresourceType))]
		[XmlElement("vmFile", typeof(string))]
		[XmlElement("vmToolUsed", typeof(string))]
		[XmlChoiceIdentifier("ItemsElementName")]
		public object[] Items;

		[XmlElement("ItemsElementName")]
		[XmlIgnore]
		public ItemsChoiceType[] ItemsElementName;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceCpuType")]
	[XmlRoot(ElementName = "labresourceCpuType")]
	public class LabresourceCpuType
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceOsType")]
	[XmlRoot(ElementName = "labresourceOsType")]
	public class LabresourceOsType
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceSoftware")]
	[XmlRoot(ElementName = "labresourceSoftware")]
	public class LabresourceSoftware
	{

		[XmlElement("softwareInstall")]
		public List<LabresourceSoftwareSoftwareInstall> SoftwareInstall;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceSoftwareSoftwareInstall")]
	[XmlRoot(ElementName = "labresourceSoftwareSoftwareInstall")]
	public class LabresourceSoftwareSoftwareInstall
	{

		[XmlAttribute(AttributeName = "id")]
		public string Id;

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "installLocation")]
		public string InstallLocation;

		[XmlAttribute(AttributeName = "executable")]
		public string Executable;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceType")]
	[XmlRoot(ElementName = "labresourceType")]
	public class LabresourceType
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[XmlType(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IncludeInSchema = false)]
	public enum ItemsChoiceType
	{

		[XmlEnum(Name = "administrativeStatus")]
		AdministrativeStatus,

		[XmlEnum(Name = "cpuArchitecture")]
		CpuArchitecture,

		[XmlEnum(Name = "cpuArchitectureWidth")]
		CpuArchitectureWidth,

		[XmlEnum(Name = "cpuManufacturer")]
		CpuManufacturer,

		[XmlEnum(Name = "cpuSpeed")]
		CpuSpeed,

		[XmlEnum(Name = "cpuType")]
		CpuType,

		[XmlEnum(Name = "diskspace")]
		Diskspace,

		[XmlEnum(Name = "fullyQualifiedDomainName")]
		FullyQualifiedDomainName,

		[XmlEnum(Name = "hostname")]
		Hostname,

		[XmlEnum(Name = "imageType")]
		ImageType,

		[XmlEnum(Name = "ipAddress")]
		IpAddress,

		[XmlEnum(Name = "memory")]
		Memory,

		[XmlEnum(Name = "name")]
		Name,

		[XmlEnum(Name = "operationalStatus")]
		OperationalStatus,

		[XmlEnum(Name = "osKernelWidth")]
		OsKernelWidth,

		[XmlEnum(Name = "osLocale")]
		OsLocale,

		[XmlEnum(Name = "osType")]
		OsType,

		[XmlEnum(Name = "primaryMacAddress")]
		PrimaryMacAddress,

		[XmlEnum(Name = "resourcegroup")]
		Resourcegroup,

		[XmlEnum(Name = "software")]
		Software,

		[XmlEnum(Name = "systemId")]
		SystemId,

		[XmlEnum(Name = "tdm")]
		Tdm,

		[XmlEnum(Name = "type")]
		Type,

		[XmlEnum(Name = "vmFile")]
		VmFile,

		[XmlEnum(Name = "vmToolUsed")]
		VmToolUsed,
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "job")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "job")]
	public class Job : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "provider")]
		public string Provider;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "jobresult")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "jobresult")]
	public class Jobresult : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "startTime")]
		public DateTime StartTime;

		[XmlElement(ElementName = "endTime")]
		public DateTime EndTime;

		[XmlElement(ElementName = "duration")]
		public int Duration;

		[XmlIgnore]
		public bool DurationSpecified;

		[XmlElement(ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "result")]
		public string Result;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "externalId")]
		public string ExternalId;

		[XmlElement(ElementName = "labresource")]
		public HrefAndID Labresource;

		[XmlElement(ElementName = "job")]
		public HrefAndID Job;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceattribute")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "labresourceattribute")]
	public class Labresourceattribute : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(DataType = "anyURI", ElementName = "uuidIdentifier")]
		public string UuidIdentifier;

		[XmlElement(ElementName = "externalIdentifier")]
		public string ExternalIdentifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "parent")]
		public LabresourceattributeParent Parent;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "labresourceattributeParent")]
	[XmlRoot(ElementName = "labresourceattributeParent")]
	public class LabresourceattributeParent
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "teamarea")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "teamarea")]
	public class Teamarea : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "summary")]
		public string Summary;

		[XmlElement(ElementName = "id")]
		public string Id;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement("contributor")]
		public List<TeamareaContributor> Contributor;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "teamareaContributor")]
	[XmlRoot(ElementName = "teamareaContributor")]
	public class TeamareaContributor
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://schema.ibm.com/vega/2008/", AttributeName = "id")]
		public string Id;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "contributor")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "contributor")]
	public class Contributor
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "userid")]
		public string Userid;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "emailaddress")]
		public string Emailaddress;

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "workitem")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "workitem")]
	public class WorkItem : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(ElementName = "type")]
		public string Type;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "creationtime")]
		public DateTime Creationtime;

		[XmlElement(ElementName = "resolvedtime")]
		public DateTime Resolvedtime;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "summary")]
		public string Summary;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(ElementName = "authorid")]
		public WorkitemAuthorid Authorid;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "ownerid")]
		public WorkitemOwnerid Ownerid;

		[XmlElement(ElementName = "resolution")]
		public string Resolution;

		[XmlElement(ElementName = "resolverid")]
		public WorkitemResolverid Resolverid;

		[XmlElement(ElementName = "stateid")]
		public string Stateid;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(ElementName = "severity")]
		public string Severity;

		[XmlElement(ElementName = "priority")]
		public string Priority;

		[XmlElement(ElementName = "filedagainst")]
		public string Filedagainst;

		[XmlElement(ElementName = "plannedfor")]
		public string Plannedfor;

		[XmlElement(ElementName = "duedate")]
		public DateTime Duedate;

		[XmlElement(ElementName = "tags")]
		public string Tags;

		[XmlElement(ElementName = "oldstate")]
		public string Oldstate;

		[XmlElement(ElementName = "statechangedate")]
		public DateTime Statechangedate;

		[XmlArray(ElementName = "changestates")]
		[XmlArrayItem("changestate", IsNullable = false)]
		public List<WorkitemChangestate> Changestates;

		[XmlArray(ElementName = "customAttributes")]
		[XmlArrayItem("customAttribute", IsNullable = false)]
		public List<CustomAttributesCustomAttribute> CustomAttributes;

		public virtual bool ShouldSerializeChangestates()
		{
			return ((this.Changestates != null)
						&& (this.Changestates.Count > 0));
		}

		public virtual bool ShouldSerializeCustomAttributes()
		{
			return ((this.CustomAttributes != null)
						&& (this.CustomAttributes.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "workitemAuthorid")]
	[XmlRoot(ElementName = "workitemAuthorid")]
	public class WorkitemAuthorid
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "workitemOwnerid")]
	[XmlRoot(ElementName = "workitemOwnerid")]
	public class WorkitemOwnerid
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "workitemResolverid")]
	[XmlRoot(ElementName = "workitemResolverid")]
	public class WorkitemResolverid
	{

		[XmlAttribute(Form = XmlSchemaForm.Qualified, Namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", DataType = "anyURI", AttributeName = "resource")]
		public string Resource;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "workitemChangestate")]
	[XmlRoot(ElementName = "workitemChangestate")]
	public class WorkitemChangestate
	{

		[XmlElement(ElementName = "stateid")]
		public string Stateid;

		[XmlElement(ElementName = "oldstate")]
		public string Oldstate;

		[XmlElement(ElementName = "newstate")]
		public string Newstate;

		[XmlElement(ElementName = "statechangedate")]
		public DateTime Statechangedate;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "tooladapter")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "tooladapter")]
	public class ToolAdapter : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "type")]
		public string Type;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "pollinginterval")]
		public int Pollinginterval;

		[XmlIgnore]
		public bool PollingintervalSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "lastheartbeat")]
		public long Lastheartbeat;

		[XmlIgnore]
		public bool LastheartbeatSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "hostname")]
		public string Hostname;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "ipaddress")]
		public string Ipaddress;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "macaddress")]
		public string Macaddress;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "fqdn")]
		public string Fqdn;

		[XmlElement("capability", Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1")]
		public List<string> Capability;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "instructions")]
		public Instructions Instructions;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "tasks")]
		public Tasks Tasks;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", TypeName = "instructions")]
	[XmlRoot(ElementName = "instructions")]
	public class Instructions
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", TypeName = "tasks")]
	[XmlRoot(ElementName = "tasks")]
	public class Tasks
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertask")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "adaptertask")]
	public class Adaptertask : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "selectedAdapterId")]
		public string SelectedAdapterId;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "type")]
		public string Type;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "progress")]
		public int Progress;

		[XmlIgnore]
		public bool ProgressSpecified;

		[XmlArray(ElementName = "variables")]
		[XmlArrayItem("variable", IsNullable = false)]
		public List<VariablesVariable> Variables;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "taken")]
		public bool Taken;

		[XmlIgnore]
		public bool TakenSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "managedbyadapter")]
		public bool Managedbyadapter;

		[XmlIgnore]
		public bool ManagedbyadapterSpecified;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "fullpath")]
		public string Fullpath;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "shareprefix")]
		public string Shareprefix;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "relativepath")]
		public string Relativepath;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "ewi")]
		public Ewi Ewi;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "updateURL")]
		public UpdateURL UpdateURL;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "resultURL")]
		public ResultURL ResultURL;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "suiteLogURL")]
		public SuiteLogURL SuiteLogURL;

		[XmlElement("task", Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1")]
		public List<Task> Task;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "message")]
		public Message Message;

		[XmlArray(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", ElementName = "properties")]
		[XmlArrayItem("property", IsNullable = false)]
		public List<PropertiesProperty> Properties;

		[XmlElement("remotescript", typeof(AdaptertaskRemotescript))]
		[XmlElement("testscript", typeof(AdaptertaskTestscript))]
		public object Item;

		[XmlElement(ElementName = "executionworkitem")]
		public AdaptertaskExecutionworkitem Executionworkitem;

		[XmlElement(ElementName = "testsuite")]
		public AdaptertaskTestsuite Testsuite;

		[XmlElement(ElementName = "testsuitelog")]
		public AdaptertaskTestsuitelog Testsuitelog;

		[XmlElement(ElementName = "resource")]
		public AdaptertaskResource Resource;

		[XmlElement(ElementName = "executionresult")]
		public AdaptertaskExecutionresult Executionresult;

		[XmlElement(ElementName = "buildrecord")]
		public HrefAndID Buildrecord;

		[XmlElement(ElementName = "markerAny")]
		public string MarkerAny;

		[XmlAnyElement]
		public List<System.Xml.XmlElement> Any;

		public virtual bool ShouldSerializeVariables()
		{
			return ((this.Variables != null)
						&& (this.Variables.Count > 0));
		}

		public virtual bool ShouldSerializeProperties()
		{
			return ((this.Properties != null)
						&& (this.Properties.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", TypeName = "ewi")]
	[XmlRoot(ElementName = "ewi")]
	public class Ewi
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", TypeName = "updateURL")]
	[XmlRoot(ElementName = "updateURL")]
	public class UpdateURL
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", TypeName = "resultURL")]
	[XmlRoot(ElementName = "resultURL")]
	public class ResultURL
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", TypeName = "suiteLogURL")]
	[XmlRoot(ElementName = "suiteLogURL")]
	public class SuiteLogURL
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", TypeName = "task")]
	[XmlRoot(ElementName = "task")]
	public class Task
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", TypeName = "message")]
	[XmlRoot(ElementName = "message")]
	public class Message
	{

		[XmlAttribute(AttributeName = "statusCode")]
		public string StatusCode;

		[XmlText]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/task/v0.1", TypeName = "propertiesProperty")]
	[XmlRoot(ElementName = "propertiesProperty")]
	public class PropertiesProperty
	{

		[XmlAttribute(AttributeName = "propertyName")]
		public string PropertyName;

		[XmlAttribute(AttributeName = "propertyType")]
		public string PropertyType;

		[XmlAttribute(AttributeName = "propertyValue")]
		public string PropertyValue;

		[XmlText]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskRemotescript")]
	[XmlRoot(ElementName = "adaptertaskRemotescript")]
	public class AdaptertaskRemotescript
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskTestscript")]
	[XmlRoot(ElementName = "adaptertaskTestscript")]
	public class AdaptertaskTestscript
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskExecutionworkitem")]
	[XmlRoot(ElementName = "adaptertaskExecutionworkitem")]
	public class AdaptertaskExecutionworkitem
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskTestsuite")]
	[XmlRoot(ElementName = "adaptertaskTestsuite")]
	public class AdaptertaskTestsuite
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskTestsuitelog")]
	[XmlRoot(ElementName = "adaptertaskTestsuitelog")]
	public class AdaptertaskTestsuitelog
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskResource")]
	[XmlRoot(ElementName = "adaptertaskResource")]
	public class AdaptertaskResource
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "adaptertaskExecutionresult")]
	[XmlRoot(ElementName = "adaptertaskExecutionresult")]
	public class AdaptertaskExecutionresult
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "instructions1")]
	[XmlRoot("instructions1", Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false)]
	public class Instructions1
	{

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", ElementName = "workavailable")]
		public bool Workavailable;

		[XmlIgnore]
		public bool WorkavailableSpecified;

		[XmlElement("instruction", Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1")]
		public List<Instruction> Instruction;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/qmadapter/v0.1", TypeName = "instruction")]
	[XmlRoot(ElementName = "instruction")]
	public class Instruction
	{

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(DataType = "anyURI", AttributeName = "taskId")]
		public string TaskId;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "approvals")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "approvals")]
	public class Approvals
	{

		[XmlElement("approvalDescriptor")]
		public List<ApprovalRequest> ApprovalDescriptor;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "customAttributes")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "customAttributes")]
	public class CustomAttributes
	{

		[XmlElement("customAttribute")]
		public List<CustomAttributesCustomAttribute> CustomAttribute;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "suiteelements")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "suiteelements")]
	public class Suiteelements
	{

		[XmlElement("suiteelement")]
		public List<SuiteelementsSuiteelement> Suiteelement;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "buildrecord")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "buildrecord")]
	public class Buildrecord : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "starttime")]
		public DateTime Starttime;

		[XmlElement(ElementName = "endtime")]
		public DateTime Endtime;

		[XmlElement(ElementName = "status")]
		public string Status;

		[XmlElement(ElementName = "providerTypeId")]
		public string ProviderTypeId;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "builddefinition")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "builddefinition")]
	public class Builddefinition : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "state")]
		public string State;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "providerTypeId")]
		public string ProviderTypeId;

		[XmlElement(ElementName = "status")]
		public string Status;

		[XmlElement("buildrecord")]
		public List<HrefAndID> Buildrecord;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "project")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "project")]
	public class Project : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement("alias")]
		public List<Alias> Aliases;

		[XmlElement("category")]
		public List<ProjectCategory> Categories;

		[XmlArray(ElementName = "settingsids")]
		[XmlArrayItem("settingid", IsNullable = false)]
		public List<string> Settingsids;

		public virtual bool ShouldSerializeSettingsids()
		{
			return ((this.Settingsids != null)
						&& (this.Settingsids.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "projectCategory")]
	[XmlRoot(ElementName = "projectCategory")]
	public class ProjectCategory
	{

		[XmlAttribute(AttributeName = "scope")]
		public string Scope;

		[XmlAttribute(AttributeName = "term")]
		public string Term;

		[XmlAttribute(AttributeName = "value")]
		public string Value;

		[XmlAttribute(AttributeName = "termUUID")]
		public string TermUUID;

		[XmlAttribute(AttributeName = "valueUUID")]
		public string ValueUUID;

		[XmlAttribute(AttributeName = "isDefault")]
		public bool IsDefault;

		[XmlIgnore]
		public bool IsDefaultSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "automationparameter")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "automationparameter")]
	public class Automationparameter
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "value")]
		public string Value;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "automationtask")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "automationtask")]
	public class Automationtask
	{

		[XmlElement(ElementName = "job")]
		public HrefAndID Job;

		[XmlElement(ElementName = "labresource")]
		public HrefAndID Labresource;

		[XmlElement("automationparameter")]
		public List<Automationparameter> Automationparameter;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executiontask")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "executiontask")]
	public class Executiontask
	{

		[XmlElement("executionworkitem", typeof(ExecutiontaskExecutionworkitem))]
		[XmlElement("suiteexecutionrecord", typeof(ExecutiontaskSuiteexecutionrecord))]
		public object Item;

		[XmlElement("executionstep")]
		public List<Executionstep> Executionstep;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutiontaskExecutionworkitem")]
	[XmlRoot(ElementName = "ExecutiontaskExecutionworkitem")]
	public class ExecutiontaskExecutionworkitem : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutiontaskSuiteexecutionrecord")]
	[XmlRoot(ElementName = "ExecutiontaskSuiteexecutionrecord")]
	public class ExecutiontaskSuiteexecutionrecord : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionstep")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "executionstep")]
	public class Executionstep
	{

		[XmlElement("remotescript", typeof(ExecutionstepRemotescript))]
		[XmlElement("testscript", typeof(ExecutionstepTestscript))]
		public object Item;

		[XmlElement(ElementName = "tooladapter")]
		public HrefAndID Tooladapter;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutionstepRemotescript")]
	[XmlRoot(ElementName = "ExecutionstepRemotescript")]
	public class ExecutionstepRemotescript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "ExecutionstepTestscript")]
	[XmlRoot(ElementName = "ExecutionstepTestscript")]
	public class ExecutionstepTestscript : HrefAndID { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "sequencestep")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "sequencestep")]
	public class Sequencestep
	{

		[XmlElement(ElementName = "externalstepid")]
		public string Externalstepid;

		[XmlElement("automationtask", typeof(Automationtask))]
		[XmlElement("executiontask", typeof(Executiontask))]
		public object Item;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionsequence")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "executionsequence")]
	public class ExecutionSequence : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "owner")]
		public Owner Owner;

		[XmlElement(ElementName = "isBvt")]
		public bool IsBvt;

		[XmlIgnore]
		public bool IsBvtSpecified;

		[XmlElement(ElementName = "runOnFailedBuild")]
		public bool RunOnFailedBuild;

		[XmlIgnore]
		public bool RunOnFailedBuildSpecified;

		[XmlElement(ElementName = "scheduleStart")]
		public DateTime ScheduleStart;

		[XmlIgnore]
		public bool ScheduleStartSpecified;

		[XmlElement(ElementName = "associatedBuildDefinition")]
		public HrefAndID AssociatedBuildDefinition;

		[XmlElement(ElementName = "jobscheduler")]
		public HrefAndID Jobscheduler;

		[XmlElement("sequencestep")]
		public List<Sequencestep> Sequencestep;

		[XmlElement(ElementName = "testcell")]
		public HrefAndID Testcell;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionsequenceresult")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "executionsequenceresult")]
	public class Executionsequenceresult : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "isRunning")]
		public bool IsRunning;

		[XmlIgnore]
		public bool IsRunningSpecified;

		[XmlElement(ElementName = "startTime")]
		public DateTime StartTime;

		[XmlElement(ElementName = "endTime")]
		public DateTime EndTime;

		[XmlElement(ElementName = "overallResult")]
		public string OverallResult;

		[XmlElement(ElementName = "associatedBuildRecord")]
		public HrefAndID AssociatedBuildRecord;

		[XmlElement(ElementName = "executionSequence")]
		public HrefAndID ExecutionSequence;

		[XmlElement("stepResult")]
		public List<ExecutionsequenceresultStepResult> StepResult;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "executionsequenceresultStepResult")]
	[XmlRoot(ElementName = "executionsequenceresultStepResult")]
	public class ExecutionsequenceresultStepResult
	{

		[XmlAttribute(AttributeName = "type")]
		public string Type;

		[XmlAttribute(AttributeName = "result")]
		public string Result;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "catalog")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "catalog")]
	public class Catalog : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement("type", Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1")]
		public List<Type> Type;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "type")]
	[XmlRoot(ElementName = "type")]
	public class Type
	{

		[XmlElement("type1")]
		public List<Type> Type1;

		[XmlElement("attribute")]
		public List<TypeAttribute> Attribute;

		[XmlElement("enumeration")]
		public List<TypeEnumeration> Enumeration;

		[XmlArray(ElementName = "attributePath")]
		[XmlArrayItem("orderedComponent", typeof(AttributePathOrderedComponent), IsNullable = false)]
		public List<AttributePathOrderedComponent> AttributePath;

		[XmlElement("testPlanCoverageType")]
		public List<TypeTestPlanCoverageType> TestPlanCoverageType;

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		[XmlAttribute(AttributeName = "externalId")]
		public string ExternalId;

		[XmlAttribute(AttributeName = "typeId")]
		public int TypeId;

		[XmlIgnore]
		public bool TypeIdSpecified;

		[XmlAttribute(AttributeName = "hidden")]
		public bool Hidden;

		public virtual bool ShouldSerializeAttributePath()
		{
			return ((this.AttributePath != null)
						&& (this.AttributePath.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "typeAttribute")]
	[XmlRoot(ElementName = "typeAttribute")]
	public class TypeAttribute
	{

		[XmlElement("primitiveType", typeof(TypeAttributePrimitiveType))]
		[XmlElement("referenceType", typeof(TypeAttributeReferenceType))]
		public object Item;

		[XmlElement(ElementName = "cardinality")]
		public TypeAttributeCardinality Cardinality;

		[XmlAttribute(AttributeName = "hidden")]
		public bool Hidden;

		[XmlAttribute(AttributeName = "name")]
		public string Name;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "typeAttributePrimitiveType")]
	[XmlRoot(ElementName = "typeAttributePrimitiveType")]
	public enum TypeAttributePrimitiveType
	{

		StringType,

		NumericType,

		TimeStampType,

		UserType,

		GroupType,
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "typeAttributeReferenceType")]
	[XmlRoot(ElementName = "typeAttributeReferenceType")]
	public class TypeAttributeReferenceType
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "typeAttributeCardinality")]
	[XmlRoot(ElementName = "typeAttributeCardinality")]
	public enum TypeAttributeCardinality
	{

		ZERO_OR_ONE,

		ZERO_OR_MORE,

		ONE,

		ONE_OR_MORE,
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "typeEnumeration")]
	[XmlRoot(ElementName = "typeEnumeration")]
	public class TypeEnumeration
	{

		[XmlElement("value")]
		public List<string> Value;

		[XmlAttribute(AttributeName = "name")]
		public string Name;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "attributePathOrderedComponent")]
	[XmlRoot(ElementName = "attributePathOrderedComponent")]
	public class AttributePathOrderedComponent
	{

		[XmlAttribute(DataType = "anyURI", AttributeName = "href")]
		public string Href;

		[XmlText]
		public List<string> Text;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/catalog/v0.1", TypeName = "typeTestPlanCoverageType")]
	[XmlRoot(ElementName = "typeTestPlanCoverageType")]
	public class TypeTestPlanCoverageType
	{

		[XmlArray(ElementName = "attributePath")]
		[XmlArrayItem("orderedComponent", IsNullable = false)]
		public List<AttributePathOrderedComponent> AttributePath;

		[XmlAttribute(AttributeName = "name")]
		public string Name;

		public virtual bool ShouldSerializeAttributePath()
		{
			return ((this.AttributePath != null)
						&& (this.AttributePath.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "snapshot")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "snapshot")]
	public class Snapshot
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "description")]
		public string Description;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(ElementName = "revision")]
		public int Revision;

		[XmlIgnore]
		public bool RevisionSpecified;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "propfind")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "propfind")]
	public class Propfind
	{

		[XmlArray(ElementName = "snapshots")]
		[XmlArrayItem("snapshot", IsNullable = false)]
		public List<Snapshot> Snapshots;

		public virtual bool ShouldSerializeSnapshots()
		{
			return ((this.Snapshots != null)
						&& (this.Snapshots.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false)]
	public class Error
	{

		[XmlElement(ElementName = "statusCode")]
		public int StatusCode;

		[XmlIgnore]
		public bool StatusCodeSpecified;

		[XmlElement(ElementName = "message")]
		public string Message;

		[XmlElement(ElementName = "trace")]
		public string Trace;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "variables")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "variables")]
	public class Variables
	{

		[XmlElement("variable")]
		public List<VariablesVariable> Variable;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "foldercontent")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "foldercontent")]
	public class Foldercontent
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "fullpath")]
		public string Fullpath;

		[XmlArray(ElementName = "files")]
		[XmlArrayItem("file", IsNullable = false)]
		public List<FoldercontentFile> Files;

		[XmlArray(ElementName = "folders")]
		[XmlArrayItem("folder", IsNullable = false)]
		public List<FoldercontentFolder> Folders;

		public virtual bool ShouldSerializeFiles()
		{
			return ((this.Files != null)
						&& (this.Files.Count > 0));
		}

		public virtual bool ShouldSerializeFolders()
		{
			return ((this.Folders != null)
						&& (this.Folders.Count > 0));
		}
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "foldercontentFile")]
	[XmlRoot(ElementName = "foldercontentFile")]
	public class FoldercontentFile
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "urn")]
		public string Urn;

		[XmlElement(ElementName = "relativepath")]
		public string Relativepath;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "foldercontentFolder")]
	[XmlRoot(ElementName = "foldercontentFolder")]
	public class FoldercontentFolder
	{

		[XmlElement(ElementName = "name")]
		public string Name;

		[XmlElement(ElementName = "urn")]
		public string Urn;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "jobscheduler")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "jobscheduler")]
	public class JobScheduler : ReportableArtifact
	{

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "identifier")]
		public string Identifier;

		[XmlElement(ElementName = "webId")]
		public int WebId;

		[XmlIgnore]
		public bool WebIdSpecified;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "title")]
		public string Title;

		[XmlElement(Namespace = "http://jazz.net/xmlns/alm/v0.1/", ElementName = "updated")]
		public DateTime Updated;

		[XmlElement(Namespace = "http://purl.org/dc/elements/1.1/", ElementName = "creator")]
		public Creator Creator;

		[XmlElement(ElementName = "description")]
		public string Description;

		[XmlElement(ElementName = "nextTriggerDate")]
		public DateTime NextTriggerDate;

		[XmlElement("buildschedule", typeof(BuildSchedule))]
		[XmlElement("dailyschedule", typeof(DailySchedule))]
		[XmlElement("onetimeschedule", typeof(OneTimeSchedule))]
		[XmlElement("weeklyschedule", typeof(Weeklyschedule))]
		public Schedule Item;

		[XmlElement(ElementName = "scheduledjob")]
		public ScheduledJob ScheduledJob;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "buildschedule")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "buildschedule")]
	public class BuildSchedule : Schedule
	{

		[XmlElement(ElementName = "runOnFailedBuild")]
		public bool RunOnFailedBuild;

		[XmlIgnore]
		public bool RunOnFailedBuildSpecified;

		[XmlElement(ElementName = "builddefinition")]
		public HrefAndID BuildDefinition;
	}

	[XmlInclude(typeof(TimeSchedule))]
	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "schedule")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = true, ElementName = "schedule")]
	public class Schedule
	{

		[XmlElement(DataType = "date", ElementName = "activationDate")]
		public DateTime ActivationDate;

		[XmlElement(DataType = "date", ElementName = "expiryDate")]
		public DateTime ExpiryDate;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "timeschedule")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = true, ElementName = "timeschedule")]
	public class TimeSchedule : Schedule
	{

		[XmlElement(DataType = "time", ElementName = "triggerTime")]
		public DateTime TriggerTime;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "dailyschedule")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "dailyschedule")]
	public class DailySchedule : TimeSchedule { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "onetimeschedule")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "onetimeschedule")]
	public class OneTimeSchedule : TimeSchedule { }

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "weeklyschedule")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "weeklyschedule")]
	public class Weeklyschedule : TimeSchedule
	{

		[XmlElement("weekday")]
		public List<string> WeekDay;
	}

	[GeneratedCode("System.Xml", "4.0.30319.233")]
	[Serializable]
	[DebuggerStepThrough]
	[DesignerCategory("code")]
	[XmlType(AnonymousType = true, Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", TypeName = "scheduledjob")]
	[XmlRoot(Namespace = "http://jazz.net/xmlns/alm/qm/v0.1/", IsNullable = false, ElementName = "scheduledjob")]
	public class ScheduledJob
	{

		[XmlElement(ElementName = "operation")]
		public string Operation;

		[XmlElement("executionSequence")]
		public HrefAndID Item;
	}
}
