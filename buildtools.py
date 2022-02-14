import sys


def print_help():
    available_tools = {
        "db": {
            "description": "database and liquibase managing tools",
            "commands": [
                {"name": "generateChangeLog", "description": "a"},
                {"name": "updateDatabase", "description": "b"}
            ]
        }
    }

    print("Available build tools:\n")

    for name, description in available_tools.items():
        print("{0} - {1}".format(name, description["description"]))
        for available_command in description["commands"]:
            print("{0}{1} {2} - {3}".format(u"\u0009",
                                            name,
                                            available_command["name"],
                                            available_command["description"]))

    print("\nUsage: buildtools [tool] [command]")


def tool_db(args):
    user_command = args[0]

    if user_command == "generateChangeLog":
        print('generating change log...')
    elif user_command == "updateDatabase":
        print("updating database...")
    else:
        print_help()


if __name__ == "__main__":
    tool = (sys.argv[1] if len(sys.argv) > 2 else None)
    command = sys.argv[2:]

    if tool == "db":
        tool_db(command)
    else:
        print_help()
