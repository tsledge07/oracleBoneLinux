# oracleBoneLinux
This project serves as a backend starter project to more effectively develop and test additional functionality for the Oracle Bone Scripts project. The main language used is Python3 and the Django framework and JSON is used for writing and managing some elements of the database developed using MongoDB.

Pipenv is the virtual environment manager for development of this project. It is an open-source, ongoing project thats is built off of Python3's virtual environments. All packages within the project are maintained by the Pipfile and Pipfile.lock.

This project was developed on Pop!_OS, an Ubuntu derived Linux distribution

## Setup
### Initial Setup
After cloning the repository or downloading and extracting the zip file, run the following command to install all the project dependencies
```
pipenv install
```
To view the current project layout, use the following command in the root folder:
```
tree .
```
### Django Setup
To start the django development server in your browser, in the root project folder, run the following commands:
```
python manage.py migrate
python manage.py runserver
```
To view in the browser, search http://127.0.0.1:8000/ in your browser

To stop the server, type in **Ctrl + C** in your terminal
### MongoDB Setup



## Project Directory Overview
### ./code-reference/
    - any complete or incomplete reference files for code
### ./docs/
    - directory for documentation files
### ./image_extraction/
    - directory of image extraction files developed by Randolph Stapp
### ./oracleBoneLinux/
    - main project directory controlling the django configuration files
### ./pages/
    - django related files for controlling django web components
    - django uses the model, view, controller architecture so this is where you add 
    your front end component controllers 
### ./papers/
    - directory of extra resources and academic papers used during research
### ./springResearch/
    - copy of the image_extraction directory
    - the original directory was configured on a MacOS device while the rest of the original project was developed on a windows device. In order to test some functionality, a copy was made and tested here

## Future Research
### Using ML to Identify and Classify Scripts by Radicals

- We want to find and organize the oracle bone scripts by the radical
- the radical is kinda like a letter in english characters
- 214 total radicals possible
- 1 - 17 stroke chars 
-   https://www.asianlanguageschool.com/chinese-characters-radicals-stroke-order/

### Else

1. Categorize by Generation and Radical
2. Create a Database for current scripts - to help in visualization
    - 1500 recognized
    - 50k available
3. Use original forms to explain the current/modern characters exist
4. CSS - clustering visualization
5. Idea -- Tracing the changes in charaters through different geenrations

- Cloud Server - AWS
- Dockerize ----> Environment 
- Python Animations based on Scripts