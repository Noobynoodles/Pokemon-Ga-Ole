"""Reproduce known bugs without modifying the uploaded Java source."""
from pathlib import Path
import subprocess, tempfile, shutil, json
ROOT=Path(__file__).resolve().parents[1]
BUILD=ROOT/'build'
BUILD.mkdir(exist_ok=True)
compiler=([shutil.which('javac')] if shutil.which('javac') else ['java','com.sun.tools.javac.Main'])
p=subprocess.run(compiler+['-Xlint:all','-d',str(BUILD)]+[str(p) for p in sorted((ROOT/'src').glob('*.java'))]+[str(ROOT/'tests/BugProbe.java')],text=True,capture_output=True)
print('COMPILE:',p.returncode,p.stderr)
if p.returncode: raise SystemExit(p.returncode)
results=[]
def run(name, main, data=None, stdin='', args=()):
    with tempfile.TemporaryDirectory() as tmp:
        folder=Path(tmp)
        if data is not None:
            d=folder/'src/data';d.mkdir(parents=True)
            (d/'pokemon.txt').write_text((ROOT/'test-fixtures/pokemon.txt').read_text())
            for filename,content in data.items(): (d/filename).write_text(content)
        p=subprocess.run(['java','-cp',str(BUILD),main,*args],cwd=folder,input=stdin,text=True,capture_output=True,timeout=12)
        result=dict(name=name,exit_code=p.returncode,output=p.stdout+p.stderr)
        results.append(result)
        print(name, 'exit='+str(p.returncode), '\n'+result['output'][-1400:])
run('main menu exit without data','Main',stdin='3\n')
run('new game without catalogue','Main',stdin='1\nTest\n')
run('fixture new game and exit','Main',{},'1\nTest\n5\nn\n3\n')
run('non-numeric Pokemon details','Main',{},'1\nTest\n1\n1\nx\n')
run('cancel Pokemon details repeats prompt','Main',{},'1\nTest\n1\n1\n-1\n')
run('damage arithmetic','BugProbe',args=['damage'])
run('players share Pokemon object','BugProbe',{'players.txt':'Alice\n1, \nBob\n1, \n'},args=['alias'])
run('empty collection cannot reload','BugProbe',{'players.txt':'Alice\n\n'},args=['load'])
run('missing save directory','BugProbe',args=['save'])
run('duplicate wild selection','BugProbe',{},args=['wild'])
run('competing battle Scanners','BugProbe',args=['input'])
run('winning battle and catch','BugProbe',args=['victory'])
run('save game and exit','Main',{},'1\nTest\n4\n\n5\nn\n3\n')
(ROOT/'tests/results.json').write_text(json.dumps(results,indent=2))
print('Results:',ROOT/'tests/results.json')
