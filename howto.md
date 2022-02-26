## Часть 1
___
####Работа со своим репозиторием  
Добавить удаленные репозиторий:   
`git remote add my2021 https://github.com/DafterPlay/KotlinAsFirst2021.git`  
Создали ветку `backport`:  
`git branch backport`   
С помощью `fetch` загрузили репозиторий локально:   
`git featch my2021`  
Перешли в созданную ранее ветку:  
`git checkout backport`  
Сделал `cherryPick` интересующих меня коммитов (используя `ide`)  
Сделал коммит (используя `ide`)  
## Часть 2
___
####Работа с репозиторием напарника  
Аналогичные действия:  
`git remote add nikita2021 https://github.com/amersmer/KotlinAsFirst2021.git`  
`git branch backport1`   
`git featch nikita2021`  
`git checkout backport1`  
Сделал `cherryPick` интересующих меня коммитов (используя `ide`)  
## Часть 3
___
####Слияние веток в `master`
Перешел в ветку `master`:  
`git checkout master`  
Используя `ide` кликнул пкм по ветке `backport`, нажал `Merge selected into current`   
Аналогично с `backport2`, устранива конфликты в пользу ветки `master`
## Часть 4
___
####Добавление файлов remotes.txt и howto.md   
Используя `ide` создал файл remotes.txt   
Ввел в терминал `git remote -v` и скопировал информацию в файл   
Сделал коммит и пуш используя `ide`   
Используя `ide` создал файл howto.md и заполнил его  
Сделал коммит и пуш используя `ide` 