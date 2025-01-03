# Copyright (c) 2025 - Nathanne Isip
# This file is part of Ambassador.
# 
# N8 is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published
# by the Free Software Foundation, either version 3 of the License,
# or (at your option) any later version.
# 
# N8 is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with N8. If not, see <https://www.gnu.org/licenses/>.

import os
import re
import shutil

def replace_in_file(filename, original, replacement):
    try:
        with open(filename, 'r') as infile:
            content = infile.read()

        updated_content = content.replace(original, replacement)
        with open(filename, 'w') as outfile:
            outfile.write(updated_content)

    except FileNotFoundError:
        print(f"Error: {filename} not found.")
        exit()

    except Exception as e:
        print(f"An error occurred: {e}")
        exit()

def is_valid_java_class_name(name):
    return re.fullmatch(r'[A-Z][a-zA-Z0-9_]*', name) is not None

def is_valid_java_package(package):
    return re.fullmatch(r'([a-z][a-zA-Z0-9_]*)(\.[a-z][a-zA-Z0-9_]*)*', package) is not None

project_name = input("Project name: ")
if not is_valid_java_class_name(project_name):
    print(
        "Invalid project name. It must start "
        "with an uppercase letter and contain "
        "only alphanumeric characters or underscores."
    )
    exit()

package_name = input("Project package: ")
if not is_valid_java_package(package_name):
    print(
        "Invalid package name. It must start "
        "with a lowercase letter and be "
        "dot-separated."
    )
    exit()

shutil.rmtree(os.path.join("Ambassador", ".git"))
shutil.rmtree(os.path.join("Ambassador", "docs"))
shutil.rmtree(os.path.join("Ambassador", "misc"))
shutil.rmtree(os.path.join("Ambassador", "src", "com"))

os.remove(os.path.join("Ambassador", ".gitignore"))
os.remove(os.path.join("Ambassador", ".gitattributes"))
os.remove(os.path.join("Ambassador", "Doxyfile"))
os.remove(os.path.join("Ambassador", "README.md"))
os.remove(os.path.join("Ambassador", ".idea", "vcs.xml"))
# os.remove(os.path.join("Ambassador", "setup.py"))

shutil.move("Ambassador", project_name)
shutil.move(
    os.path.join(project_name, "Ambassador.iml"),
    os.path.join(project_name, project_name + ".iml")
)
shutil.move(
    os.path.join(
        project_name, ".idea", "artifacts",
        "ambassador_template.xml"
    ),
    os.path.join(
        project_name, ".idea", "artifacts",
        project_name + ".xml"
    )
)
shutil.move(
    os.path.join(
        project_name, ".idea",
        "runConfigurations",
        "ambassador_template.xml"
    ),
    os.path.join(
        project_name, ".idea",
        "runConfigurations",
        project_name + ".xml"
    )
)

folder_path = os.path.join(
    project_name,
    "src",
    package_name
).replace('.', os.sep)
os.makedirs(
    folder_path,
    exist_ok=True
)
os.makedirs(
    os.path.join(folder_path, "controllers"),
    exist_ok=True
)

replace_in_file(
    os.path.join(project_name, "META-INF", "MANIFEST.MF"),
    "com.example.app.WebApplication",
    package_name + "." + project_name
)

replace_in_file(
    os.path.join(
        project_name, ".idea", "artifacts",
        project_name + ".xml"
    ),
    "ambassador-template",
    project_name
)
replace_in_file(
    os.path.join(
        project_name, ".idea", "artifacts",
        project_name + ".xml"
    ),
    "ambassador_template",
    project_name
)
replace_in_file(
    os.path.join(
        project_name, ".idea", "artifacts",
        project_name + ".xml"
    ),
    "Ambassador",
    project_name
)

replace_in_file(
    os.path.join(
        project_name, ".idea", "runConfigurations",
        project_name + ".xml"
    ),
    "ambassador-template",
    project_name
)
replace_in_file(
    os.path.join(
        project_name, ".idea", "runConfigurations",
        project_name + ".xml"
    ),
    "Ambassador",
    project_name
)
replace_in_file(
    os.path.join(
        project_name, ".idea", "runConfigurations",
        project_name + ".xml"
    ),
    "com.example.app.WebApplication",
    package_name + "." + project_name
)

replace_in_file(
    os.path.join(
        project_name, ".idea", "scopes",
        "web_ambassador.xml"
    ),
    "com.example.app",
    package_name
)

replace_in_file(
    os.path.join(project_name, ".idea", "modules.xml"),
    "Ambassador", project_name
)

with open(
    os.path.join(folder_path, project_name + ".java"
), "w") as main:
    main.write(
        "package " + package_name + ";\r\n"
        "\r\n"
        "import " + package_name + ".controllers.DateTime;\r\n"
        "import " + package_name + ".controllers.HomeController;\r\n"
        "\r\n"
        "import web.ambassador.core.Kernel;\r\n"
        "import java.io.IOException;\r\n"
        "\r\n"
        "public class " + project_name + " {\r\n"
        "    public static void main(String[] args) {\r\n"
        "        try(Kernel framework = new Kernel(8080)) {\r\n"
        "            framework.setupShutdownHook(\r\n"
        "                ()-> System.out.println(\"Server stopped successfully!\"),\r\n"
        "                ()-> System.out.println(\"Cannot shutdown server!\")\r\n"
        "            );\r\n"
        "\r\n"
        "            framework.registerController(HomeController.class);\r\n"
        "            framework.registerController(DateTime.class);\r\n"
        "\r\n"
        "            framework.setMaxThreads(Runtime.getRuntime().availableProcessors() * 2);\r\n"
        "            framework.start();\r\n"
        "        }\r\n"
        "        catch(IOException e) {\r\n"
        "            System.err.println(\"Failed to start server: \" + e.getMessage());\r\n"
        "            System.exit(1);\r\n"
        "        }\r\n"
        "        catch(Exception e) {\r\n"
        "            System.err.println(\"Unexpected error: \" + e.getMessage());\r\n"
        "            System.exit(1);\r\n"
        "        }\r\n"
        "    }\r\n"
        "}\r\n"
    )

with open(
    os.path.join(
        folder_path,
        "controllers",
        "HomeController.java"
), "w") as main:
    main.write(
        "package " + package_name + ".controllers;\r\n"
        "\r\n"
        "import web.ambassador.annotations.Controller;\r\n"
        "import web.ambassador.annotations.Method;\r\n"
        "import web.ambassador.db.DatabaseManager;\r\n"
        "import web.ambassador.http.Request;\r\n"
        "import web.ambassador.http.Response;\r\n"
        "import web.ambassador.view.Component;\r\n"
        "import web.ambassador.view.Dom;\r\n"
        "import web.ambassador.view.ViewContent;\r\n"
        "\r\n"
        "@Controller\r\n"
        "public class HomeController implements Component {\r\n"
        "    @Override\r\n"
        "    @Method\r\n"
        "    public ViewContent index(Request request, Response response, DatabaseManager dbManager) {\r\n"
        "        return Dom.createPage(\r\n"
        "            Dom.head(\r\n"
        "                Dom.title(\"Your Ambassador homepage!\"),\r\n"
        "                Dom.stylesheet(\"assets/styles/bootstrap.min.css\")\r\n"
        "            ),\r\n"
        "            Dom.body(\r\n"
        "                Dom.br(),\r\n"
        "                Dom.div(\r\n"
        "                    Dom.img()\r\n"
        "                        .src(\"assets/images/logo.png\")\r\n"
        "                        .attr(\"alt\", \"Logo\")\r\n"
        "                        .attr(\"width\", \"300\")\r\n"
        "                        .addClass(\"mt-4\"),\r\n"
        "                    Dom.h2(\"Welcome to your Ambassador homepage!\")\r\n"
        "                        .addClass(\"mt-4\"),\r\n"
        "                    Dom.p(\r\n"
        "                        Dom.noTag(\"Server date and time is: \"),\r\n"
        "                        Dom.span().id(\"datetime\")\r\n"
        "                    ),\r\n"
        "                    Dom.div(\r\n"
        "                        Dom.a(\r\n"
        "                            \"https://nthnn.github.io/Ambassador\",\r\n"
        "                            \"Learn More\",\r\n"
        "                            \"_blank\"\r\n"
        "                        ).addClass(\r\n"
        "                            \"btn\",\r\n"
        "                            \"btn-primary\",\r\n"
        "                            \"d-block-inline\",\r\n"
        "                            \"mx-1\"\r\n"
        "                        ),\r\n"
        "                        Dom.a(\r\n"
        "                            \"https://github.com/nthnn/Ambassador\",\r\n"
        "                            \"GitHub\",\r\n"
        "                            \"_blank\"\r\n"
        "                        ).addClass(\r\n"
        "                            \"btn\",\r\n"
        "                            \"btn-primary\",\r\n"
        "                            \"d-block-inline\",\r\n"
        "                            \"mx-1\"\r\n"
        "                        )\r\n"
        "                    ),\r\n"
        "                    Dom.script(\r\n"
        "                        \"javascript\",\r\n"
        "                        \"\"\"\r\n"
        "                        const fetchDatetime = async ()=> {\r\n"
        "                            const response = await fetch('/datetime', {\r\n"
        "                                method: 'POST',\r\n"
        "                                headers: {\r\n"
        "                                    'Content-Type': 'application/json'\r\n"
        "                                },\r\n"
        "                                body: JSON.stringify({})\r\n"
        "                            });\r\n"
        "\r\n"
        "                            if(!response.ok)\r\n"
        "                                return;\r\n"
        "\r\n"
        "                            const data = await response.json();\r\n"
        "                            const datetimeElement = document.getElementById('datetime');\r\n"
        "\r\n"
        "                            datetimeElement.textContent = data.datetime || \"No data received\";\r\n"
        "                        };\r\n"
        "\r\n"
        "                        fetchDatetime();\r\n"
        "                        setInterval(fetchDatetime, 1000);\r\n"
        "                        \"\"\"\r\n"
        "                    )\r\n"
        "                ).attr(\"align\", \"center\")\r\n"
        "                    .addClass(\"mt-4\")\r\n"
        "            )\r\n"
        "        );\r\n"
        "    }\r\n"
        "}\r\n"
    )

with open(
    os.path.join(
        folder_path,
        "controllers",
        "DateTime.java"
), "w") as main:
    main.write(
        "package " + package_name + ".controllers;\r\n"
        "\r\n"
        "import web.ambassador.annotations.Controller;\r\n"
        "import web.ambassador.annotations.Method;\r\n"
        "import web.ambassador.db.DatabaseManager;\r\n"
        "import web.ambassador.enums.HttpStatusCode;\r\n"
        "import web.ambassador.enums.MethodType;\r\n"
        "import web.ambassador.http.Request;\r\n"
        "import web.ambassador.http.Response;\r\n"
        "import web.ambassador.view.Component;\r\n"
        "import web.ambassador.view.EmptyView;\r\n"
        "import web.ambassador.view.JsonContent;\r\n"
        "import web.ambassador.view.ViewContent;\r\n"
        "\r\n"
        "import java.util.Date;\r\n"
        "import java.util.HashMap;\r\n"
        "import java.util.Map;\r\n"
        "\r\n"
        "@Controller(path=\"/datetime\")\r\n"
        "public class DateTime implements Component {\r\n"
        "    @Override\r\n"
        "    @Method(MethodType.POST)\r\n"
        "    public ViewContent index(Request request, Response response, DatabaseManager dbManager) {\r\n"
        "        Map<String, Object> data = new HashMap<>();\r\n"
        "        data.put(\"datetime\", new Date());\r\n"
        "\r\n"
        "        return JsonContent.fromMap(data);\r\n"
        "    }\r\n"
        "\r\n"
        "    @Method\r\n"
        "    public ViewContent redirect(Request request, Response response, DatabaseManager dbManager) {\r\n"
        "        response.setStatusCode(HttpStatusCode.PERMANENT_REDIRECT);\r\n"
        "        response.getHeaders().put(\"Location\", \"/\");\r\n"
        "\r\n"
        "        return new EmptyView();\r\n"
        "    }\r\n"
        "}\r\n"
    )
