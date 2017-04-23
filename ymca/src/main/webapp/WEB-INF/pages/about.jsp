<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%@ include file="./includes/bootstrapImports.jspf"%>
<title>About</title>
</head>
<body>
	<div class="container-fluid">
		<%@ include file="./includes/header.jspf"%>
		<div class="text-center well">
			<div class="row">
				<div class="col-sm-0 col-md-3"></div>
				<div class="col-sm-12 col-md-6">
					<h2>About This Site</h2>
					<br>
						<h4>
							The purpose of the site is to show what it is possible for
							foundation degree students to create using freely available
							technologies. This site required several programming languages to
							realise, all of which have been learned during the course. The
							project is the work of three students; <strong>Jake
								Lewis</strong>, <strong>Matthew Rayner</strong>, and <strong>Myric
								Allen</strong>. We hope that this site demonstrates the potential skills
							that could be learned by a student of the course.
						</h4> <br>
							<h4>
								This site has been created as the project assignment for the
								Yeovil College level 5 Computing course. More information about
								the course can be found <a
									href="http://www.yeovil.ac.uk/Full%20Time/ITComputing/FdScComputing.html">on
									the college website</a>.
							</h4> <br>
								<h4>
									Information about the development and source code for this site
									can be found on our GitHub page at <a
										href="https://github.com/UCY-Team/yeovil_microsoft_cognitive_ai">https://github.com/UCY-Team/yeovil_microsoft_cognitive_ai</a>
								</h4> <br>
				</div>
				<div class="col-sm-0 col-md-3"></div>
			</div>
		</div>
		<div class="text-center well">
			<h2>PROGRAMMING LANGUAGES</h2>
			<br>
				<div class="row">
					<div class="col-sm-4">
						<span class="glyphicon glyphicon-list-alt"></span>
						<h4>HTML</h4>
						<p>Used to create the webpages</p>
					</div>
					<div class="col-sm-4">
						<span class="glyphicon glyphicon-pencil"></span>
						<h4>CSS</h4>
						<p>Used to style the webpages</p>
					</div>
					<div class="col-sm-4">
						<span class="glyphicon glyphicon-wrench"></span>
						<h4>JavaScript</h4>
						<p>Used to do client-side scripting</p>
					</div>
					<div class="col-sm-1 col-lg-2"></div>
					<div class="col-sm-5 col-lg-4">
						<span class="glyphicon glyphicon-cloud"></span>
						<h4>JAVA</h4>
						<p>Used to code the server</p>
					</div>
					<div class="col-sm-5 col-lg-4">
						<span class="glyphicon glyphicon-hdd"></span>
						<h4>SQL</h4>
						<p>Used to interact with the database</p>
					</div>
					<div class="col-sm-1 col-lg-2"></div>
				</div>
		</div>
		<div class="text-center well">
			<div class="row">
				<h2>THIRD-PARTY TECHNOLOGIES</h2>
				<br>
					<div class="col-sm-4">
						<h3>Microsoft Cognitive Service</h3>
						<p>
							The Yeovil Microsoft Cognitive AI uses <a
								href="http://go.microsoft.com/fwlink/?LinkID=829046">Microsoft
								Cognitive Services</a> for it's information processing modules, such
							as the webcam analysis.
						</p>
						<p>
							Microsoft will receive certain data from the Yeovil Microsoft
							Cognitive AI to provide and improve its products. To report abuse
							of the Microsoft Cognitive Services to Microsoft, please visit
							the Microsoft Cognitive Services website at <a
								href="https://www.microsoft.com/cognitive-services">https://www.microsoft.com/cognitive-services</a>,
							and use the "Report Abuse" link at the bottom of the page. For
							more information refer to the Microsoft Privacy Statement here: <a
								href="https://go.microsoft.com/fwlink/?LinkId=521839">https://go.microsoft.com/fwlink/?LinkId=521839</a>
						</p>
					</div>
					<div class="col-sm-4">
						<h3>Tomcat Catalina</h3>
						<p>The servlet container for our server, this allows us to use
							Java Servlet Pages (JSP) so that we can dynamically load parts of
							a page</p>
					</div>
					<div class="col-sm-4">
						<h3>Tomcat WebSocket</h3>
						<p>A Tomcat API that allows us to use WebSockets in our site,
							these allow two-way communication between the front-end and the
							server</p>
					</div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<h3>JSON</h3>
					<p>Short for JavaScript Object Notation, it's used to pass
						certain data between the front-end and the servers in a neat,
						object-oriented fashion</p>
				</div>
				<div class="col-sm-4">
					<h3>Apache Commons Libraries - DbUtils</h3>
					<p>The Commons DbUtils library is a small set of classes
						designed to make working with the Java Database Connectivity API
						(JDBC) easier.</p>
				</div>
				<div class="col-sm-4">
					<h3>Apache Commons Libraries - lang3</h3>
					<p>Lang provides a host of helper utilities for the java.lang
						API, notably String manipulation methods, basic numerical methods,
						object reflection, concurrency, creation and serialization and
						System properties. Additionally it contains basic enhancements to
						java.util.Date and a series of utilities dedicated to help with
						building methods, such as hashCode, toString and equals.</p>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<h3>Bootstrap</h3>
					<p>Bootstrap is the most popular HTML, CSS, and JS framework
						for developing responsive, mobile first projects on the web.
						Bootstrap enables us to make our site scale easily to mobile
						devices, as well as making it easier to have a professional
						looking site.</p>
				</div>
				<div class="col-sm-4">
					<h3>Apache Maven</h3>
					<p>Maven is a build automation tool used primarily for Java
						projects. Maven allows the easy inclusion and management of
						dependancies, as well as controling the build process.</p>
				</div>
				<div class="col-sm-4">
					<h3>Travis CI</h3>
					<p>Travis CI is a hosted, distributed continuous integration
						service used to build and test projects hosted at GitHub. Travis
						CI automatically detects when a commit has been made and pushed to
						a GitHub repository that is using Travis CI, and each time this
						happens, it will try to build the project and run tests. Our
						project uses it to ensure that each build is working.</p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>