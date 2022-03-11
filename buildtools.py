import sys
import subprocess


def print_help():
    available_tools = {
        'liquibase': {
            'description': 'database and liquibase managing tools',
            'commands': [
                {'name': 'generate',
                 'description': 'create a changelog file which describes how to re-create the current state of the database'},
                {'name': 'update',
                 'description': 'deploy any changes that are in the changelog file and that have not been deployed to the database yet'}
            ]
        }
    }

    print('Available build tools:')

    for name, description in available_tools.items():
        print()
        print('=====')
        print('{0} - {1}'.format(name, description['description']))

        for available_command in description['commands']:
            print('{0}{1} {2} - {3}'.format(u'\u0009',
                                            name,
                                            available_command['name'],
                                            available_command['description']))

    print('\nUsage: buildtools [tool] [command]')


def execute(executable_command):
    subprocess.run(executable_command, shell=True)


def tool_liquibase(args):
    user_command = args[0]

    if user_command == 'changelog':
        print('generating change log...')
    elif user_command == 'update':
        execute('mvn -pl db -Dbuild.profile.id=dev liquibase:update')
    else:
        print_help()


if __name__ == '__main__':
    tool = (sys.argv[1] if len(sys.argv) > 2 else None)
    command = sys.argv[2:]

    if tool == 'liquibase':
        tool_db(command)
    else:
        print_help()
