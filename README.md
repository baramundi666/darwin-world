# OOP Darwin World Project 2023/24

Wariant: F2 

Autorzy: 
Mateusz Król 
Maciej Makowski 


## Instrukcja obsługi:
- **HomePage**
    - ***New configuration*** - ręczne ustawienie nowej konfiguracji z mozliwością zapisu do pliku
    - ***Saved configurations*** - wczytanie z pliku zapisanej poprzednio konfiguracji
    - ***Launch Simulation*** - odpalenie symulacji o nowowybranej konfiguracji lub tej, która została wybrana z tych uprzednio zapisanych (można odpalać wielokrotnie)
- **Configuration**
    - Istnieją ograniczenia wprowadzanych danych
    - Jest możliwość wybrania opcji zapisu statystyk symulacji do pliku
- **Simulation**
    - ***Pause, Resume*** - zatrzymanie i wznowienie symulacji
    - Gdy symulacja zostanie zatrzymana, istnieje możliwość kliknięcia dowolnego zwierzęcia w celu śledzenia go - zostaje podświetlony na ***niebiesko***
    - Gdy symulacja zostanie zatrzymana, istnieje możliwość zaznaczenia wszystkich zwierząt z dominującym genotypem - zostają podświetlone na ***żółto***

&nbsp;
&nbsp;
#### Źródło kospektu projektowego:
#### https://github.com/Soamid/obiektowe-lab/tree/master/proj
&nbsp;
&nbsp;
## Projekt: Darwin World


Niniejsza treść została zaadaptowana przez Aleksandra Smywińskiego-Pohla na podstawie opisu oraz ilustracji przygotowanych przez Wojciecha Kosiora. Inspiracją dla niego była z kolei książka "Land of Lisp" Conrada Barskiego, który zaś zainspirował się artykułem w "Scientific American". A na końcu modyfikacje wprowadził Radosław Łazarz, bazując częściowo na książce "Algorytmy genetyczne i ich zastosowania" Davida E. Goldberga. Dużo ludzi jak na jeden projekcik.

### Cel projektu

Stwórzmy grę! Nie będzie to jednak gra, w którą my gramy. Zamiast tego będzie to świat, który ewoluuje na naszych oczach! Stworzymy środowisko stepów i dżungli ze zwierzakami, które biegają, buszują w zaroślach, jedzą i rozmnażają się. A po kilku milionach lat zobaczymy, że wyewoluowały w różne gatunki!

<img src="pictures/zwierzak.jpg"/>

Świat naszej gry jest dość prosty. Składa się ze zwykłej, prostokątnej połaci podzielonej na kwadratowe pola. Większość świata pokrywają stepy, na których rośnie niewiele roślin stanowiących pożywienie zwierzaków. Niektóre rejeony porasta jednak dżungla, gdzie rośliny rosną dużo szybciej. Rośliny będą wyrastały w losowych miejscach, ale ich koncentracja będzie większa w dżungli niż na stepie.

<img src="pictures/dzungla.jpg"/>

Nasze zwierzęta, które są roślinożercami, będą przemierzały ten świat w poszukiwaniu pożywienia. Każdy zwierzak ma określoną energię, która zmniejsza się co dnia. Znalezienie i zjedzenie rośliny zwiększa poziom energii o pewną wartość.

### Anatomia zwierzaka


<img src="pictures/zwierzak2.jpg"/>

Musimy śledzić kilka cech każdego zwierzaka. Po pierwsze, zarówno w przypadku rośliny jak i tych, którzy je zjadają, musimy znać koordynaty `x` i `y`. Wskazują nam one, gdzie dany zwierzak lub roślina jest na mapie.

Musimy także wiedzieć, ile energii ma dany zwierzak. To darwinowska gra o przetrwanie, więc jeśli zwierzak nie zdoła zdobyć odpowiedniej ilości pożywienia, będzie głodować i zdechnie...  Energia mówi nam o tym, ile dni funkcjonowania zostało jeszcze danemu zwierzakowi. Musi ono koniecznie znaleźć więcej jedzenia, zanim jej zapas się wyczerpie.

<img src="pictures/kierunki.jpg"/>

Musimy również pamiętać, w którą stronę zwierzak jest zwrócony. Jest to ważne, ponieważ każdego dnia będzie ono poruszać się na mapie w tym właśnie kierunku. Istnieje osiem różnych możliwych pozycji i tyle samo możliwych obrotów. Obrót `0` oznacza, że zwierzak nie zmienia swojej orientacji, obrót `1` oznacza, że zwierzak obraca się o 45°, `2`, o 90°, itd. Przykładowo: jeśli zwierzak był skierowany na północ i obrót wynosi `1`, to zwierzak skierowany jest teraz na północny wschód.

Na koniec musimy także przechowywać geny zwierzaka. Każdy zwierzak ma N genów, z których każdy jest jedną liczbą z zakresu od `0` do `7`. Geny te opisują (w bardzo uproszczony sposób) schemat zachowania danej istoty. Egzystencja naszych zwierzaków ma cykliczną naturę. Każdy z nich przechowuje informację o tym, z którego fragmentu swojego genomu będzie korzystał najbliższego dnia. Podczas każdego ruchu zwierzak zmienia najpierw swoje ustawienie, obracając się zgodnie z aktualnie aktywnym genem, a potem porusza się o jedno pole w wyznaczonym kierunku. Następnie gen ulega dezaktywacji, a aktywuje się gen na prawo od niego (będzie sterował zwierzakiem kolejnego dnia). Gdy geny skończą się, to aktywacja wraca na początek ich listy. Przykładowo - genom:
`0 0 7 0 4`
oznacza, że żyjątko będzie kolejno: szło przed siebie, szło przed siebie, szło nieco w lewo, szło przed siebie, zawracało, szło przed siebie, ... - itd.

### Konsumpcja i rozmnażanie

Jedzenie jest prostym procesem. Zakładamy, że zwierzak zjada roślinę, gdy stanie na jej polu, a jego energia wzrasta wtedy o z góry zdefiniowaną wartość.

Rozmnażanie jest zwykle najciekawszą częścią każdej symulacji ze zwierzakami. Zdrowe młode może mieć tylko zdrowa para rodziców, dlatego nasze zwierzaki będą się rozmnażać tylko jeśli mają odpowiednią ilość energii. Przy reprodukcji rodzice tracą na rzecz młodego pewną część swojej energii - ta energia będzie rónocześnie stanowić startową energię ich potomka.

Urodzone zwierzę otrzymuje genotyp będący krzyżówką genotypów rodziców. Udział genów jest proporcjonalny do energii rodziców i wyznacza miejsce podziału genotypu. Przykładowo, jeśli jeden rodzic ma 50, a  drugi 150 punktów energii, to dziecko otrzyma 25% genów pierwszego oraz 75% genów drugiego rodzica. Udział ten określa miejsce przecięcia genotypu, przyjmując, że geny są uporządkowane. W pierwszym kroku losowana jest strona genotypu, z której zostanie wzięta część osobnika silniejszego, np. *prawa*. W tym przypadku dziecko otrzymałoby odcinek obejmujący 25% *lewych* genów pierwszego rodzica oraz 75% *prawych* genów drugiego rodzica. Jeśli jednak wylosowana byłaby strona *lewa*, to dziecko otrzymałoby 75% *lewych* genów silniejszego osobnika oraz 25% *prawych* genów. Na koniec mają zaś miejsce mutacje: losowa liczba (wybranych również losowo) genów potomka zmienia swoje wartości na zupełnie nowe.


### Symulacja

Symulacja każdego dnia składa się z poniższej sekwencji kroków:

1. Usunięcie martwych zwierzaków z mapy.
2. Skręt i przemieszczenie każdego zwierzaka.
3. Konsumpcja roślin, na których pola weszły zwierzaki.
4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
5. Wzrastanie nowych roślin na wybranych polach mapy.

Daną symulację opisuje szereg parametrów:

* wysokość i szerokość mapy,
* wariant mapy (wyjaśnione w sekcji poniżej),
* startowa liczba roślin,
* energia zapewniana przez zjedzenie jednej rośliny,
* liczba roślin wyrastająca każdego dnia,
* wariant wzrostu roślin (wyjaśnione w sekcji poniżej),
* startowa liczba zwierzaków,
* startowa energia zwierzaków,
* energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania),
* energia rodziców zużywana by stworzyć potomka,
* minimalna i maksymalna liczba mutacji u potomków (może być równa `0`),
* wariant mutacji (wyjaśnione w sekcji poniżej),
* długość genomu zwierzaków,
* wariant zachowania zwierzaków (wyjaśnione w sekcji poniżej).

### Warianty konfiguracji 

#### Mapa i roślinność

W przypadku mapy kluczowe jest to, jak obsługujemy jej krawędzie. Zrealizujemy następujące warianty:

* [obowiązkowo dla wszystkich] **kula ziemska** - lewa i prawa krawędź mapy zapętlają się (jeżeli zwierzak wyjdzie za lewą krawędź, to pojawi się po prawej stronie - a jeżeli za prawą, to po lewej); górna i dolna krawędź mapy to bieguny - nie można tam wejść (jeżeli zwierzak próbuje wyjść poza te krawędzie mapy, to pozostaje na polu na którym był, a jego kierunek zmienia się na odwrotny);
* [F] **zatrute owoce** - preferowany jest rozkład równomierny, ale na pewnym kwadratowym podobszarze mapy (zajmującym 20% mapy) czasem pojawiają się trujące rośliny, które zamiast dostarczać energię, odbierają ją po spożyciu. Jeśli zwierzak podczas swojego ruchu ma wejść na trującą roślinę, wykonuje test na spostrzegawczość - ma 20% szans, żeby ostatecznie wykonać ruch na inne sąsiadujące pole i uniknąć zatrucia (test może być wykonywany raz na dzień życia zwierzaka).

#### Zwierzaki

W przypadku mutacji mamy do czynienia z dwoma prostymi opcjami:

* [obowiązkowo dla wszystkich] **pełna losowość** - mutacja zmienia gen na dowolny inny gen;
* [2] **podmianka** - mutacja może też skutkować tym, że dwa geny zamienią się miejscami.

### Wymagania dla aplikacji

1. Aplikacja ma być realizowana z użyciem graficznego interfejsu użytkownika z wykorzystaniem biblioteki JavaFX.
2. Jej głównym zadaniem jest umożliwienie uruchamiania symulacji o wybranych konfiguracjach.
   1. Powinna umożliwić wybranie jednej z uprzednio przygotowanych gotowych konfiguracji,
   1. "wyklikanie" nowej konfiguracji,
   1. oraz zapisanie jej do ponownego użytku w przyszłości.
3. Uruchomienie symulacji powinno skutkować pojawieniem się nowego okna obsługującego daną symulację.
   1. Jednocześnie uruchomionych może być wiele symulacji, każda w swoim oknie, każda na osobnej mapie.
4. Sekcja symulacji ma wyświetlać animację pokazującą pozycje zwierzaków, ich energię w dowolnej formie (np. koloru lub paska zdrowia) oraz pozycje roślin - i ich zmiany.
5. Program musi umożliwiać zatrzymywanie oraz wznawianie animacji w dowolnym momencie (niezależnie dla każdej mapy - patrz niżej).
6. Program ma pozwalać na śledzenie następujących statystyk dla aktualnej sytuacji w symulacji:
   * liczby wszystkich zwierzaków,
   * liczby wszystkich roślin,
   * liczby wolnych pól,
   * najpopularniejszych genotypów,
   * średniego poziomu energii dla żyjących zwierzaków,
   * średniej długości życia zwierzaków dla martwych zwierzaków (wartość uwzględnia wszystkie nieżyjące zwierzaki - od początku symulacji),
   * średniej liczby dzieci dla żyjących zwierzaków (wartość uwzględnia wszystkie powstałe zwierzaki, a nie tylko zwierzaki powstałe w danej epoce).
7. Po zatrzymaniu programu można oznaczyć jednego zwierzaka jako wybranego do śledzenia. Od tego momentu (do zatrzymania śledzenia) UI powinien przekazywać nam informacje o jego statusie i historii:
   * jaki ma genom,
   * która jego część jest aktywowana,
   * ile ma energii,
   * ile zjadł roślin,
   * ile posiada dzieci,
   * ile posiada potomków (niekoniecznie będących bezpośrednio dziećmi),
   * ile dni już żyje (jeżeli żyje),
   * którego dnia zmarło (jeżeli żywot już skończyło).
8. Po zatrzymaniu programu powinno być też możliwe:
   * pokazanie, które ze zwierząt mają dominujący (najpopularniejszy) genotyp (np. poprzez wyróżnienie ich wizualnie),
   * pokazanie, które z pól są preferowane przez rośliny (np. poprzez wyróżnienie ich wizualnie).
9. Jeżeli zdecydowano się na to w momencie uruchamiania symulacji, to jej statystyki powinny być zapisywane (każdego dnia) do pliku CSV. Plik ten powinnien być "otwieralny" przez dowolny rozujmiejący ten format program (np. MS Excel). 
10. Aplikacja powinna być możliwa do zbudowania i uruchomienia z wykorzystaniem Gradle'a.

